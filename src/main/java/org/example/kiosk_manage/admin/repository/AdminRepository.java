package org.example.kiosk_manage.admin.repository;


import org.example.kiosk_manage.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long>, AdminRepositoryCustom {

    Optional<Admin> findByEmail(String email);
    Boolean existsAdminByEmail(String email);
}
