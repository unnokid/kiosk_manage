package org.example.kiosk_manage.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.cart.domain.Cart;
import org.example.kiosk_manage.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int orderNo;

    private String status;

    private Payment payment;

    private int total_amount;

    private int paidAmount;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin admin;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> cartList = new ArrayList<>();

    public void addAdmin(Admin admin) {
        if (Objects.isNull(admin)) {
            throw new RuntimeException();
        }
        if (this.admin != null) {
            this.admin.getOrderList().remove(this);
        }
        admin.addOrder(this);
        this.admin = admin;
    }

    public void addCart(Cart cart) {
        this.cartList.add(cart);
    }


    public void plusAmount(int amount) {
        this.total_amount += amount;
    }

}
