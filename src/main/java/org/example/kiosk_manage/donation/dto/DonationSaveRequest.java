package org.example.kiosk_manage.donation.dto;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DonationSaveRequest {

    // 재전송된 찬조를 구분하는 값. 보내지 않는 구버전 앱도 허용한다.
    @Size(max = 64, message = "요청번호는 64자 이하여야 합니다.")
    private String requestId;

    private String email;
    private String name;
    private int amount;
}
