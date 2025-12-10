package org.example.kiosk_manage.donation.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DonationSaveRequest {

    private String email;
    private String name;
    private int amount;
    private String reason;
}
