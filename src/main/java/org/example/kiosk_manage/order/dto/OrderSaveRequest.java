package org.example.kiosk_manage.order.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrderSaveRequest {

    private String email;
    private int paidAmount;
    private List<OrderCartSaveRequest> carts;
}
