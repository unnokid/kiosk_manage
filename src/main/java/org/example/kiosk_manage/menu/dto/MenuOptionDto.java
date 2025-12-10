package org.example.kiosk_manage.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MenuOptionDto {
    public Long id;
    public Long menuId;
    public String optionName;
    public int price;
}
