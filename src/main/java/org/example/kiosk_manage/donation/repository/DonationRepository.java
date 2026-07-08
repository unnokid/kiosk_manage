package org.example.kiosk_manage.donation.repository;

import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.donation.domain.Donation;
import org.example.kiosk_manage.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    @Query("select o from Donation o where o.admin.id = :adminId and function('date', o.createDate) = :date and o.status = 'Y'")
    List<Donation> findByAdminIdAndDate(@Param("adminId") Long adminId, @Param("date") LocalDate date);

    @Query("select o from Donation o where o.admin.id = :adminId and function('date', o.createDate) = :date")
    List<Donation> findAllByAdminIdAndDate(@Param("adminId") Long adminId, @Param("date") LocalDate date);
}
