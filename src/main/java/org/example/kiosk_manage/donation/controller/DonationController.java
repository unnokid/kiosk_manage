package org.example.kiosk_manage.donation.controller;

import org.example.kiosk_manage.donation.dto.DonationDeleteRequest;
import org.example.kiosk_manage.donation.dto.DonationSaveRequest;
import org.example.kiosk_manage.donation.service.DonationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/donation")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping("/plus")
    public ResponseEntity<Void> plus(@RequestBody DonationSaveRequest request) {
        donationService.plus(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/minus")
    public ResponseEntity<Void> minus(@RequestBody DonationDeleteRequest request) {
        donationService.minus(request);

        return ResponseEntity.ok().build();
    }
}
