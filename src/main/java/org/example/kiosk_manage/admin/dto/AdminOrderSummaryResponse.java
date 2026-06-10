package org.example.kiosk_manage.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.kiosk_manage.order.dto.OrderSummaryDto;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AdminOrderSummaryResponse {
    private int totalOrderCount;
    private int totalOrderPrice;
    private List<OrderSummaryDto> orderList;
}
