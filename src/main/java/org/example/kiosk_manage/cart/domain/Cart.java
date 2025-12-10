package org.example.kiosk_manage.cart.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.common.BaseEntity;
import org.example.kiosk_manage.menu.domain.Menu;
import org.example.kiosk_manage.order.domain.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    private int menu_totalamount;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    @JsonIgnore
    private Menu menu;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartOption> cartOptionList = new ArrayList<>();

    public void addOrder(Order order) {
        if (Objects.isNull(order)) {
            throw new RuntimeException();
        }
        if (this.order != null) {
            this.order.getCartList().remove(this);
        }
        order.addCart(this);
        this.order = order;
    }

    public void addMenu(Menu menu) {
        if (Objects.isNull(menu)) {
            throw new RuntimeException();
        }
        if (this.menu != null) {
            this.menu.getCartList().remove(this);
        }
        menu.addCart(this);
        this.menu = menu;
    }

    public void addCartOption(CartOption cartOption) {
        this.cartOptionList.add(cartOption);
    }

}
