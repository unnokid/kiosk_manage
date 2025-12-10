package org.example.kiosk_manage.common.converter;

import org.example.kiosk_manage.menu.domain.MenuOption;
import org.example.kiosk_manage.menu.dto.MenuOptionDto;

import java.util.List;

public class Converter {

    public static MenuOptionDto toMenuOptionDto(MenuOption menuOption) {
        if (menuOption == null) {
            return null;
        }

        return MenuOptionDto.builder()
                .id(menuOption.getId())
                .menuId(menuOption.getMenu().getId())
                .optionName(menuOption.getOptionName())
                .price(menuOption.getOptionPrice())
                .build();
    }

    public static List<MenuOptionDto> toMenuOptionDtos(List<MenuOption> options) {
        if (options == null || options.isEmpty()) {
            return List.of();
        }

        return options.stream()
                .map(Converter::toMenuOptionDto)
                .toList();
    }

}
