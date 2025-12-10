package org.example.kiosk_manage.order.controller;

import org.example.kiosk_manage.order.dto.OrderDeleteRequest;
import org.example.kiosk_manage.order.dto.OrderSaveRequest;
import org.example.kiosk_manage.order.dto.OrderSaveResponse;
import org.example.kiosk_manage.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/plus")
    public ResponseEntity<OrderSaveResponse> plus(@RequestBody OrderSaveRequest request) {
        OrderSaveResponse response = orderService.plus(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/minus")
    public ResponseEntity<Void> minus(@RequestBody OrderDeleteRequest request) {
        orderService.minus(request);

        return ResponseEntity.ok().build();
    }
}
