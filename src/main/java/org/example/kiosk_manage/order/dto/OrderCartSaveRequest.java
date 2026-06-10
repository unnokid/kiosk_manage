package org.example.kiosk_manage.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCartSaveRequest {

    private Long menuId;
    private int quantity;
    private List<CartOptionRequest> cartOptions;
}
