package org.example.kiosk_manage.admin.repository;

import jakarta.persistence.LockModeType;
import org.example.kiosk_manage.admin.domain.AdminDonationSeq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminDonationSeqRepository extends JpaRepository<AdminDonationSeq, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from AdminDonationSeq s where s.adminId = :adminId")
    Optional<AdminDonationSeq> findForUpdate(@Param("adminId") Long adminId);


    @Modifying
    @Query(value = """
        INSERT INTO admin_donation_seq (admin_id, next_no)
        VALUES (:adminId, 1)
        ON DUPLICATE KEY UPDATE next_no = next_no
        """, nativeQuery = true)
    void initIfAbsent(@Param("adminId") Long adminId);
}
