package org.example.kiosk_manage.cart.repository;

import org.example.kiosk_manage.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
