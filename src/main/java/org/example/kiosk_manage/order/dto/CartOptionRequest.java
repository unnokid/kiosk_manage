package org.example.kiosk_manage.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CartOptionRequest {
    private String optionName;
}
