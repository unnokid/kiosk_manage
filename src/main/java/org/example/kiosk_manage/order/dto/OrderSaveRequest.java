package org.example.kiosk_manage.order.dto;


import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSaveRequest {

    // 재전송된 주문을 구분하는 값. 보내지 않는 구버전 앱도 허용한다.
    @Size(max = 64, message = "요청번호는 64자 이하여야 합니다.")
    private String requestId;

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @Min(value = 0, message = "받은 금액은 0 이상이어야 합니다.")
    private int paidAmount;

    @NotBlank(message = "결제 수단은 필수입니다.")
    private String payment;

    @NotEmpty(message = "주문 항목은 최소 1개 이상이어야 합니다.")
    @Valid
    private List<OrderCartSaveRequest> carts;
}
