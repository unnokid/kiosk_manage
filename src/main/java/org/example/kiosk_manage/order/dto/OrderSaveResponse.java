package org.example.kiosk_manage.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSaveResponse {
    private Integer orderNo;
    private String createdt;
}
