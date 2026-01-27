package org.example.kiosk_manage.admin.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin_order_seq")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminOrderSeq {

    @Id
    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "next_no", nullable = false)
    private Integer nextNo;

    public static AdminOrderSeq init(Long adminId) {
        return new AdminOrderSeq(adminId, 1);
    }

    public int issue() {
        int issued = this.nextNo;
        this.nextNo = this.nextNo + 1;
        return issued;
    }
}