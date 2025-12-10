package org.example.kiosk_manage.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MenuDeleteRequest {

    private String email;
    private Long menuId;
}
