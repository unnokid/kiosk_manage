package org.example.kiosk_manage.catecory.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.common.BaseEntity;
import org.example.kiosk_manage.menu.domain.Menu;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Menu> menuList;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin admin;

    private boolean active = true;

    public void addMenu(Menu menu) {
        this.menuList.add(menu);
    }

    public void addAdmin(Admin admin) {
        if (Objects.isNull(admin)) {
            throw new RuntimeException();
        }
        if (this.admin != null) {
            this.admin.getDonationList().remove(this);
        }
        admin.addCategory(this);
        this.admin = admin;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }
}
