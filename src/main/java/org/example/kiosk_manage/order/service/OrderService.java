package org.example.kiosk_manage.order.service;

import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.admin.domain.AdminOrderSeq;
import org.example.kiosk_manage.admin.repository.AdminOrderSeqRepository;
import org.example.kiosk_manage.admin.repository.AdminRepository;
import org.example.kiosk_manage.cart.domain.Cart;
import org.example.kiosk_manage.cart.domain.CartOption;
import org.example.kiosk_manage.cart.repository.CartOptionRepository;
import org.example.kiosk_manage.cart.repository.CartRepository;
import org.example.kiosk_manage.common.exception.BadRequestException;
import org.example.kiosk_manage.menu.domain.Menu;
import org.example.kiosk_manage.menu.domain.MenuOption;
import org.example.kiosk_manage.menu.repository.MenuRepository;
import org.example.kiosk_manage.order.domain.Order;
import org.example.kiosk_manage.order.domain.Payment;
import org.example.kiosk_manage.order.dto.*;
import org.example.kiosk_manage.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final AdminRepository adminRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    private final AdminOrderSeqRepository adminOrderSeqRepository;

    public OrderService(AdminRepository adminRepository, MenuRepository menuRepository, OrderRepository orderRepository, AdminOrderSeqRepository adminOrderSeqRepository) {
        this.adminRepository = adminRepository;
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.adminOrderSeqRepository = adminOrderSeqRepository;
    }

    @Transactional
    public OrderSaveResponse plus(OrderSaveRequest request) {

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("해당 아이디를 가진 고객이 존재하지 않습니다."));

        adminOrderSeqRepository.initIfAbsent(admin.getId());

        AdminOrderSeq seq = adminOrderSeqRepository.findForUpdate(admin.getId()).orElseThrow();
        int nextNo = seq.issue();


        //Order 객체 생성
        Order order = Order.builder()
                .status("Y")
                .admin(admin)
                .orderNo(nextNo)
                .paidAmount(request.getPaidAmount())
                .payment(parsePayment(request.getPayment()))
                .cartList(new ArrayList<>())
                .build();

        for (OrderCartSaveRequest c : request.getCarts()) {
            //메뉴 데이터
            Menu menu = menuRepository.findById(c.getMenuId())
                    .orElseThrow(() -> new BadRequestException("해당 메뉴는 존재하지 않습니다."));
            if (!menu.isActive()) {
                throw new BadRequestException("삭제된 메뉴는 주문할 수 없습니다.");
            }

            //메뉴가격
            int price = menu.getPrice();

            Set<String> cartOptions = c.getCartOptions().stream()
                    .map(CartOptionRequest::getOptionName)
                    .collect(Collectors.toSet());

            int optionPrice = menu.getMenuOptionList().stream()
                    .filter(option -> cartOptions.contains(option.getOptionName()))
                    .mapToInt(MenuOption::getOptionPrice)
                    .sum();

            //추가 -- 여기서 받은금액을 넣어야할거같은데요
            order.plusAmount((price + optionPrice) * c.getQuantity());

            Cart cart = Cart.builder()
                    .quantity(c.getQuantity())
                    .menu_totalamount((price + optionPrice) * c.getQuantity())
                    .cartOptionList(new ArrayList<>())
                    .build();

            cart.addOrder(order);
            cart.addMenu(menu);

            //cart <-> cartOption 연관관계 (옵션 하나당 CartOption 1개)
            List<MenuOption> collect = menu.getMenuOptionList().stream()
                    .filter(option -> cartOptions.contains(option.getOptionName()))
                    .toList();
            for (MenuOption menuOption : collect) {
                CartOption cartOption = CartOption.builder().build();
                cartOption.addCart(cart);
                cartOption.addMenuOption(menuOption);
            }
        }

        //TODO: 찬조하고 그냥 가져가는 분들은 가격을 매치시킬 수 없음
//        if (order.getTotal_amount() > request.getPaidAmount()) {
//            throw new BadRequestException("받은 금액이 적습니다.");
//        }


        Order or = orderRepository.save(order);
        return OrderSaveResponse.builder()
                .orderNo(or.getOrderNo())
                .createdt(or.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    private Payment parsePayment(String value) {
        try {
            return Payment.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("유효하지 않은 결제 수단입니다: " + value + " (가능한 값: KKP, CASH, ETC)");
        }
    }

    @Transactional
    public void minus(OrderDeleteRequest request) {

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("해당 아이디를 가진 고객이 존재하지 않습니다."));

        Order target = admin.getOrderList().stream()
                .filter(order -> order.getId().equals(request.getOrderId()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("해당 주문기록은 존재하지 않습니다."));

        target.cancel();
    }

}
