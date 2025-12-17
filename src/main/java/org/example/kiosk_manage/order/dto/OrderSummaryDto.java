package org.example.kiosk_manage.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDto {
    private int orderNo;
    private String createDate;
    private String status;
    private String payment;
    private int total_amount;
    private int paidAmount;
    private List<OrderMenuDto> menuList;
}

