package org.example.kiosk_manage.menu.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.kiosk_manage.cart.domain.CartOption;
import org.example.kiosk_manage.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu_option")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MenuOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionName;

    private int optionPrice;

    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @OneToMany(mappedBy = "menuOption")
    private List<CartOption> cartOptionList = new ArrayList<>();

    public void addMenuOption(Menu menu) {
        if (Objects.isNull(menu)) {
            throw new RuntimeException();
        }
        if (this.menu != null) {
            this.menu.getMenuOptionList().remove(this);
        }
        menu.addMenuOption(this);
        this.menu = menu;
    }

    public void addCartOption(CartOption cartOption) {
        this.cartOptionList.add(cartOption);
    }


    public void deactivate() { this.active = false; }
    public void activate() { this.active = true; }
}
