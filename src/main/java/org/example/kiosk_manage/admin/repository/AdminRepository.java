package org.example.kiosk_manage.admin.repository;


import org.example.kiosk_manage.admin.domain.Admin;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long>, AdminRepositoryCustom {

    Optional<Admin> findByEmail(String email);
    Boolean existsAdminByEmail(String email);

    // 같은 매장의 메뉴·카테고리 변경이 동시에 들어오면 차례대로 처리하기 위한 잠금
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Admin a where a.email = :email")
    Optional<Admin> findByEmailForUpdate(@Param("email") String email);
}
