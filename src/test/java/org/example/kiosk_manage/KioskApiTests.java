package org.example.kiosk_manage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.admin.repository.AdminRepository;
import org.example.kiosk_manage.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/** 메모리 DB(H2)만 사용한다. application.properties의 MySQL에는 접속하지 않는다. */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:kiosk;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "logging.file.name=build/test.log",
        "dev-auth.password=test-pass"
})
@AutoConfigureMockMvc
class KioskApiTests {

    private static final String PASSWORD = "passw0rd!";

    @Autowired MockMvc mvc;
    @Autowired AdminRepository adminRepository;
    @Autowired OrderRepository orderRepository;

    private final ObjectMapper json = new ObjectMapper();
    private String storeA;
    private String storeB;

    @BeforeEach
    void signup() throws Exception {
        storeA = newStore();
        storeB = newStore();
    }

    @Test
    void sameRequestIdIsSavedOnce() throws Exception {
        long menu = createMenu(storeA, "커피", "[]");
        String requestId = UUID.randomUUID().toString();

        JsonNode first = ok(order(storeA, menu, requestId));
        JsonNode second = ok(order(storeA, menu, requestId));

        assertEquals(first.get("orderNo"), second.get("orderNo"));
        assertEquals(1, orders(storeA).size());
    }

    @Test
    void differentOrMissingRequestIdIsSavedSeparately() throws Exception {
        long menu = createMenu(storeA, "커피", "[]");
        ok(order(storeA, menu, UUID.randomUUID().toString()));
        ok(order(storeA, menu, UUID.randomUUID().toString()));
        ok(order(storeA, menu, null)); // 구버전 앱
        ok(order(storeA, menu, null));
        assertEquals(4, orders(storeA).size());
    }

    @Test
    void sameRequestIdSentConcurrentlyIsSavedOnce() throws Exception {
        long menu = createMenu(storeA, "커피", "[]");
        ok(order(storeA, menu, null)); // 채번 행을 미리 만들어 둔다
        String requestId = UUID.randomUUID().toString();

        ExecutorService pool = Executors.newFixedThreadPool(4);
        List<Callable<MockHttpServletResponse>> calls = new ArrayList<>();
        for (int i = 0; i < 4; i++) calls.add(() -> order(storeA, menu, requestId));
        for (Future<MockHttpServletResponse> f : pool.invokeAll(calls)) ok(f.get());
        pool.shutdown();

        assertEquals(2, orders(storeA).size());
    }

    @Test
    void sameRequestIdInAnotherStoreIsIndependent() throws Exception {
        String requestId = UUID.randomUUID().toString();
        ok(order(storeA, createMenu(storeA, "커피", "[]"), requestId));
        ok(order(storeB, createMenu(storeB, "커피", "[]"), requestId));
        assertEquals(1, orders(storeA).size());
        assertEquals(1, orders(storeB).size());
    }

    @Test
    void cancelUsesIdAndCancelsOnlyThatOrder() throws Exception {
        long menuA = createMenu(storeA, "커피", "[]");
        long menuB = createMenu(storeB, "커피", "[]");
        ok(order(storeA, menuA, null));
        ok(order(storeB, menuB, null));
        ok(order(storeB, menuB, null));

        JsonNode target = byOrderNo(orders(storeB), 2);
        assertNotEquals(target.get("orderNo").asLong(), target.get("id").asLong());

        ok(send("/order/minus", "{\"email\":\"%s\",\"orderId\":%d}"
                .formatted(storeB, target.get("id").asLong())));

        assertEquals("Y", byOrderNo(orders(storeB), 1).get("status").asText());
        assertEquals("N", byOrderNo(orders(storeB), 2).get("status").asText());
        assertEquals("Y", byOrderNo(orders(storeA), 1).get("status").asText());
        // 다른 매장의 주문은 취소할 수 없다
        assertEquals(400, send("/order/minus", "{\"email\":\"%s\",\"orderId\":%d}"
                .formatted(storeA, target.get("id").asLong())).getStatus());
    }

    @Test
    void donationCancelUsesId() throws Exception {
        for (String s : List.of(storeA, storeB, storeB)) {
            ok(send("/donation/plus", "{\"email\":\"%s\",\"name\":\"찬조\",\"amount\":1000}".formatted(s)));
        }
        JsonNode list = read("/admin/summary/donation", storeB).get("donationList");
        JsonNode target = list.get(1);
        ok(send("/donation/minus", "{\"email\":\"%s\",\"donationId\":%d}"
                .formatted(storeB, target.get("id").asLong())));

        for (JsonNode d : read("/admin/summary/donation", storeB).get("donationList")) {
            assertEquals(d.get("id").equals(target.get("id")) ? "N" : "Y", d.get("status").asText());
        }
    }

    @Test
    void menuOfAnotherStoreCannotBeOrdered() throws Exception {
        long menuB = createMenu(storeB, "커피", "[]");
        assertEquals(400, order(storeA, menuB, null).getStatus());
        assertEquals(0, orders(storeA).size());
    }

    @Test
    void categoryOfAnotherStoreCannotBeUsed() throws Exception {
        long categoryB = createCategory(storeB, "음료");
        assertEquals(400, send("/menu/plus", menuBody(storeA, "커피", categoryB, "[]")).getStatus());
    }

    @Test
    @ExtendWith(OutputCaptureExtension.class)
    void categoryWithMenuCannotBeDeletedAndIsNotLoggedAsServerError(CapturedOutput output) throws Exception {
        long menu = createMenu(storeA, "커피", "[]");
        long category = read("/category/all", storeA).get(0).get("id").asLong();
        String body = "{\"email\":\"%s\",\"categoryId\":%d}".formatted(storeA, category);

        MockHttpServletResponse rejected = send("/category/minus", body);
        assertEquals(400, rejected.getStatus());
        assertEquals("삭제 불가: 메뉴가 존재합니다.",
                json.readTree(rejected.getContentAsString(StandardCharsets.UTF_8)).get("message").asText());
        assertEquals(1, read("/category/all", storeA).size());
        assertTrue(output.getAll().contains("[BadRequest]"));
        assertFalse(output.getAll().contains("[IllegalState]"));

        ok(send("/menu/minus", "{\"email\":\"%s\",\"menuId\":%d}".formatted(storeA, menu)));
        ok(send("/category/minus", body));
        assertEquals(0, read("/category/all", storeA).size());
    }

    @Test
    void menuWithoutOptionsCanBeCreated() throws Exception {
        long category = createCategory(storeA, "음료");
        ok(send("/menu/plus", menuBody(storeA, "물", category, "[]")));
        ok(send("/menu/plus", menuBody(storeA, "차", category, "null")));
        ok(send("/menu/plus", menuBody(storeA, "커피", category, "[{\"option\":\"ICE\",\"optionPrice\":500}]")));
        JsonNode menus = read("/menu/all", storeA);
        assertEquals(3, menus.size());
    }

    @Test
    void passwordIsHashedAndLoginWorks() throws Exception {
        Admin admin = adminRepository.findByEmail(storeA).orElseThrow();
        assertNotEquals(PASSWORD, admin.getPassword());
        assertTrue(admin.getPassword().startsWith("$2"));

        ok(send("/admin/login", "{\"email\":\"%s\",\"password\":\"%s\"}".formatted(storeA, PASSWORD)));
        assertEquals(400, send("/admin/login",
                "{\"email\":\"%s\",\"password\":\"wrong1!aa\"}".formatted(storeA)).getStatus());
        assertEquals(400, send("/admin/login", "{\"email\":\"%s\"}".formatted(storeA)).getStatus());
    }

    @Test
    void printFailAcceptsOldAndNewApp() throws Exception {
        ok(send("/order/1/print-fail", ""));
        ok(send("/order/1/print-fail?email=" + storeA, ""));
    }

    // ---------------------------------------------------------------- helpers

    private String newStore() throws Exception {
        String email = UUID.randomUUID() + "@test.com";
        ok(send("/admin/signup", "{\"email\":\"%s\",\"password\":\"%s\",\"passwordCheck\":\"%s\",\"name\":\"매장\"}"
                .formatted(email, PASSWORD, PASSWORD)));
        return email;
    }

    private long createCategory(String email, String name) throws Exception {
        ok(send("/category/plus", "{\"email\":\"%s\",\"name\":\"%s\"}".formatted(email, name)));
        return read("/category/all", email).get(0).get("id").asLong();
    }

    private long createMenu(String email, String name, String options) throws Exception {
        long category = createCategory(email, "음료");
        ok(send("/menu/plus", menuBody(email, name, category, options)));
        return read("/menu/all", email).get(0).get("id").asLong();
    }

    private String menuBody(String email, String name, long category, String options) {
        return "{\"email\":\"%s\",\"name\":\"%s\",\"price\":3000,\"categoryId\":%d,\"options\":%s}"
                .formatted(email, name, category, options);
    }

    private MockHttpServletResponse order(String email, long menuId, String requestId) throws Exception {
        String id = requestId == null ? "" : "\"requestId\":\"" + requestId + "\",";
        return send("/order/plus", ("{%s\"email\":\"%s\",\"paidAmount\":3000,\"payment\":\"CASH\","
                + "\"carts\":[{\"menuId\":%d,\"quantity\":1,\"cartOptions\":[]}]}").formatted(id, email, menuId));
    }

    private JsonNode orders(String email) throws Exception {
        return read("/admin/summary/order", email).get("orderList");
    }

    private JsonNode byOrderNo(JsonNode orders, int orderNo) {
        for (JsonNode o : orders) if (o.get("orderNo").asInt() == orderNo) return o;
        throw new AssertionError("주문 없음: " + orderNo);
    }

    private MockHttpServletResponse send(String path, String body) throws Exception {
        return mvc.perform(post("/api/v1" + path).header("X-DEV-PASS", "test-pass")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn().getResponse();
    }

    private JsonNode read(String path, String email) throws Exception {
        return ok(mvc.perform(get("/api/v1" + path).header("X-DEV-PASS", "test-pass")
                .param("email", email)).andReturn().getResponse());
    }

    private JsonNode ok(MockHttpServletResponse response) throws Exception {
        String body = response.getContentAsString(StandardCharsets.UTF_8);
        assertEquals(200, response.getStatus(), body);
        return body.isEmpty() ? null : json.readTree(body);
    }
}
