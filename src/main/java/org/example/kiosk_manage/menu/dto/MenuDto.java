package org.example.kiosk_manage.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MenuDto {
    public Long id;
    public String name;
    public String category;
    public Integer price;
    public List<MenuOptionDto> options;
}
