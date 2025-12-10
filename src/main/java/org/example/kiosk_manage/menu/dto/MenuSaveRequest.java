package org.example.kiosk_manage.menu.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MenuSaveRequest {

    private String email;
    private String name;
    private int price;
    private int count;
    private Long categoryId;
    private List<MenuOptionSaveRequest> options;
}
