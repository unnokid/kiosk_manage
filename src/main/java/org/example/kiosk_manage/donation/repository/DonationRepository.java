package org.example.kiosk_manage.donation.repository;

import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.donation.domain.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DonationRepository extends JpaRepository<Donation, Long>, DonationRepositoryCustom {
    @Query("select max(d.donationNo) from Donation d where d.admin = :admin")
    Optional<Integer> findMaxDonationNoByAdmin(@Param("admin") Admin admin);
}
