package org.example.kiosk_manage.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderMenuDto {
    private String menuName;
    private String optionName;
    private int quantity;
}
