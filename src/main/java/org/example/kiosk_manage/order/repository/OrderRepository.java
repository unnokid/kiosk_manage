package org.example.kiosk_manage.order.repository;

import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long>, OrderRepositoryCustom {
    @Query("select max(o.orderNo) from Order o where o.admin = :admin")
    Optional<Integer> findMaxOrderNoByAdmin(@Param("admin") Admin admin);
}
