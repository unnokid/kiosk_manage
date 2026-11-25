package org.example.kiosk_manage.order.repository;

import org.example.kiosk_manage.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    Optional<Order> findByAdminIdAndRequestId(Long adminId, String requestId);

    @Query("select o from Order o where o.admin.id = :adminId and function('date', o.createDate) = :date and o.status = 'Y' order by o.id")
    List<Order> findByAdminIdAndDate(@Param("adminId") Long adminId, @Param("date") LocalDate date);

    @Query("select o from Order o where o.admin.id = :adminId and function('date', o.createDate) = :date order by o.id")
    List<Order> findAllByAdminIdAndDate(@Param("adminId") Long adminId, @Param("date") LocalDate date);
}
