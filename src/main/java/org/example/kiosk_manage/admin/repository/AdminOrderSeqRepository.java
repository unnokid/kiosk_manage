package org.example.kiosk_manage.admin.repository;

import jakarta.persistence.LockModeType;

import org.example.kiosk_manage.admin.domain.AdminOrderSeq;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminOrderSeqRepository extends JpaRepository<AdminOrderSeq, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from AdminOrderSeq s where s.adminId = :adminId")
    Optional<AdminOrderSeq> findForUpdate(@Param("adminId") Long adminId);

    @Modifying
    @Query(value = """
    INSERT INTO admin_order_seq (admin_id, next_no)
    VALUES (:adminId, 1)
    ON DUPLICATE KEY UPDATE next_no = next_no
    """, nativeQuery = true)
    void initIfAbsent(@Param("adminId") Long adminId);
}
