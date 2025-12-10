package org.example.kiosk_manage.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrderCartSaveRequest {

    private Long menuId;
    private int quantity;
    private List<CartOptionRequest> cartOptions;
}
