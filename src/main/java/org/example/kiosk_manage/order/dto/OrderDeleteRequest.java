package org.example.kiosk_manage.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OrderDeleteRequest {

    private String email;
    private Long OrderId;
}
