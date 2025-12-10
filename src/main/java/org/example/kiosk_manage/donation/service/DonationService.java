package org.example.kiosk_manage.donation.service;

import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.admin.repository.AdminRepository;
import org.example.kiosk_manage.common.exception.BadRequestException;
import org.example.kiosk_manage.donation.domain.Donation;
import org.example.kiosk_manage.donation.dto.DonationDeleteRequest;
import org.example.kiosk_manage.donation.dto.DonationSaveRequest;
import org.example.kiosk_manage.donation.repository.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationService {

    private final AdminRepository adminRepository;
    private final DonationRepository donationRepository;

    public DonationService(AdminRepository adminRepository, DonationRepository donationRepository) {
        this.adminRepository = adminRepository;
        this.donationRepository = donationRepository;
    }

    public void plus(DonationSaveRequest request) {

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("해당 아이디를 가진 고객이 존재하지 않습니다."));

        Integer nextNo = donationRepository.findMaxDonationNoByAdmin(admin)
                .orElse(0) + 1;

        donationRepository.save(Donation.builder()
                .donationNo(nextNo)
                .name(request.getName())
                .amount(request.getAmount())
                .reason(request.getReason())
                .admin(admin)
                .build());
    }

    @Transactional
    public void minus(DonationDeleteRequest request) {

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("해당 아이디를 가진 고객이 존재하지 않습니다."));

        Donation donation = admin.getDonationList().stream()
                .filter(o -> o.getId().equals(request.getDonationId()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("해당 찬조값을 가진 데이터가 존재하지 않습니다."));

        donationRepository.delete(donation);
    }
}
