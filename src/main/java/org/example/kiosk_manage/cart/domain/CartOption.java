package org.example.kiosk_manage.cart.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.kiosk_manage.menu.domain.Menu;
import org.example.kiosk_manage.menu.domain.MenuOption;
import org.example.kiosk_manage.order.domain.Order;

import java.util.Objects;

@Entity
@Table(name = "cart_option")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CartOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "menuOption_id", nullable = true)
    @JsonIgnore
    private MenuOption menuOption;

    public void addCart(Cart cart) {
        if (Objects.isNull(cart)) {
            throw new RuntimeException();
        }
        if (this.cart != null) {
            this.cart.getCartOptionList().remove(this);
        }
        cart.addCartOption(this);
        this.cart = cart;
    }

    public void addMenuOption(MenuOption menuOption) {
        if (Objects.isNull(menuOption)) {
            throw new RuntimeException();
        }
        if (this.menuOption != null) {
            this.menuOption.getCartOptionList().remove(this);
        }
        menuOption.addCartOption(this);
        this.menuOption = menuOption;
    }
}
