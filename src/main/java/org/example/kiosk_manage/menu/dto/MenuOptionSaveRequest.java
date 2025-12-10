package org.example.kiosk_manage.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MenuOptionSaveRequest {

    private String option;
    private int optionPrice;
}
