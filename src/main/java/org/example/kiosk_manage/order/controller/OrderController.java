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
    public ResponseEntity<Void> printFail(@PathVariable int orderNo) {
        log.warn("[프린터 실패] orderNo={}", orderNo);
        return ResponseEntity.ok().build();
    }
}
