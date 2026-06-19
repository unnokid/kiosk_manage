package org.example.kiosk_manage.admin.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.kiosk_manage.catecory.domain.Category;
import org.example.kiosk_manage.common.BaseEntity;
import org.example.kiosk_manage.donation.domain.Donation;
import org.example.kiosk_manage.order.domain.Order;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "admin")
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    @OneToMany(mappedBy = "admin")
    @Builder.Default
    private List<Donation> donationList = new ArrayList<>();

    @OneToMany(mappedBy = "admin")
    @Builder.Default
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "admin")
    @Builder.Default
    private List<Category> categoryList = new ArrayList<>();


    public boolean matchPassword(String password){
        //일단 기본적인 문자열 비교 -> 이후에 암호화된 비밀번호로 체크할것
        return this.password.equals(password) ;
    }

    public void addDonation(Donation donation) {
        this.donationList.add(donation);
    }

    public void addOrder(Order order) {
        this.orderList.add(order);
    }

    public void addCategory(Category category) {
        this.categoryList.add(category);
    }



}
