package org.example.kiosk_manage.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.kiosk_manage.order.dto.OrderDeleteRequest;
import org.example.kiosk_manage.order.dto.OrderSaveRequest;
import org.example.kiosk_manage.order.dto.OrderSaveResponse;
import org.example.kiosk_manage.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/plus")
    public ResponseEntity<OrderSaveResponse> plus(@RequestBody @Valid OrderSaveRequest request) {
        OrderSaveResponse response = orderService.plus(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/minus")
    public ResponseEntity<Void> minus(@RequestBody OrderDeleteRequest request) {
        orderService.minus(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderNo}/print-fail")
    public ResponseEntity<Void> printFail(@PathVariable int orderNo,
                                          @RequestParam(required = false) String email,
                                          @RequestParam(required = false) String type) {
        // 줄바꿈을 제거해 로그 위조를 막는다
        String store = email == null ? "-" : email.replaceAll("[\\r\\n]", "_");
        // 영수증 종류: ORDER(주문) / REPRINT(재출력) / CANCEL(취소). 예전 앱은 보내지 않는다.
        // 로그가 길어지지 않도록 앞 20자만 남긴다
        String kind = type == null ? "ORDER" : type.replaceAll("[^A-Za-z_]", "");
        if (kind.length() > 20) kind = kind.substring(0, 20);
        log.warn("[프린터 실패] email={}, orderNo={}, type={}", store, orderNo, kind);
        return ResponseEntity.ok().build();
    }
}
