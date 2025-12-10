package org.example.kiosk_manage.admin.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AdminLoginRequest {

    private String email;
    private String password;
}
