package org.example.kiosk_manage.catecory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CategoryDeleteRequest {

    private String email;
    private Long categoryId;
}
