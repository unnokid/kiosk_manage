package org.example.kiosk_manage.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.kiosk_manage.donation.domain.Donation;
import org.example.kiosk_manage.order.domain.Payment;
import org.example.kiosk_manage.order.dto.OrderSummaryDto;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class AdminSummaryResponse {

    private int totalAmount;
    private int totalCount;

    private Map<Payment,Long> paymentCount;
    private Map<Payment,Long> paymentAmount;

    private int totalDonationCount;
    private int totalDonationAmount;

    private int totalOrderCount;
    private int totalOrderPrice;
    private Map<String, Long> menuCountMap;

}
