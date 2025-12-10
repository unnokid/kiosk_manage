package org.example.kiosk_manage.donation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DonationDeleteRequest {

    private String email;
    private Long donationId;
}
