package org.example.kiosk_manage.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin_donation_seq")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminDonationSeq {
    @Id
    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "next_no", nullable = false)
    private Integer nextNo;

    public static AdminDonationSeq init(Long adminId) {
        return new AdminDonationSeq(adminId, 1);
    }

    public int issue() {
        int issued = this.nextNo;
        this.nextNo = this.nextNo + 1;
        return issued;
    }
}
