package org.example.kiosk_manage.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.kiosk_manage.donation.domain.Donation;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AdminDonationSummaryResponse {

    private int totalDonationCount;
    private int totalDonationAmount;
    private List<Donation> donationList;
}
