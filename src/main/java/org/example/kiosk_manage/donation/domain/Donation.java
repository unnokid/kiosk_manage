package org.example.kiosk_manage.donation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.common.BaseEntity;

import java.util.Objects;

@Entity
@Table(name = "donation", uniqueConstraints =
        @UniqueConstraint(name = "uk_donation_admin_requestid", columnNames = {"admin_id", "request_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Donation extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int donationNo;

    // 재전송된 찬조를 구분하는 값 (구버전 앱은 null)
    @Column(name = "request_id", length = 64)
    @JsonIgnore
    private String requestId;

    private String name;

    private String status;
    
    private int amount;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin admin;

    public void cancel() {
        this.status = "N";
    }

    public void addAdmin(Admin admin) {
        if (Objects.isNull(admin)) {
            throw new RuntimeException();
        }
        if (this.admin != null) {
            this.admin.getDonationList().remove(this);
        }
        admin.addDonation(this);
        this.admin = admin;
    }
}
