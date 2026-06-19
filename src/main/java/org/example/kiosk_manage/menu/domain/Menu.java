package org.example.kiosk_manage.menu.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.kiosk_manage.cart.domain.Cart;
import org.example.kiosk_manage.catecory.domain.Category;
import org.example.kiosk_manage.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int price;

    private int remain;

    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuOption> menuOptionList = new ArrayList<>();

    @OneToMany(mappedBy = "menu")
    private List<Cart> cartList = new ArrayList<>();

    public void addCategory(Category category) {
        if (Objects.isNull(category)) {
            throw new RuntimeException();
        }
        if (this.category != null) {
            this.category.getMenuList().remove(this);
        }
        category.addMenu(this);
        this.category = category;
    }

    public void addMenuOption(MenuOption menuOption) {
        this.menuOptionList.add(menuOption);
    }

    public void addCart(Cart cart) {
        this.cartList.add(cart);
    }

    public boolean minus(int count) {
        if (this.remain >= count) {
            this.remain -= count;
            return true;
        }
        return false;
    }

    public void deactivate() { this.active = false; }
    public void activate() { this.active = true; }
}
