package org.example.kiosk_manage.catecory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CategoryListRequest {
    public String email;
}
