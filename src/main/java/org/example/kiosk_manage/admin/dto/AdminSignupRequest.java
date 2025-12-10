package org.example.kiosk_manage.admin.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AdminSignupRequest {

    private String email;
    private String password;
    private String passwordCheck;
    private String name;
}
