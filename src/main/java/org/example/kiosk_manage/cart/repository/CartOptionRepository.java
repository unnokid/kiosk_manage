package org.example.kiosk_manage.cart.repository;

import org.example.kiosk_manage.cart.domain.CartOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartOptionRepository extends JpaRepository<CartOption,Long> {
}
