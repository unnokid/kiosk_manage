package org.example.kiosk_manage.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCartSaveRequest {

    @NotNull(message = "메뉴 ID는 필수입니다.")
    private Long menuId;

    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private int quantity;

    @Builder.Default
    private List<CartOptionRequest> cartOptions = new ArrayList<>();
}
