package org.example.kiosk_manage.admin.controller;

import org.example.kiosk_manage.admin.dto.AdminLoginRequest;
import org.example.kiosk_manage.admin.dto.AdminSignupRequest;
import org.example.kiosk_manage.admin.dto.AdminSummaryResponse;
import org.example.kiosk_manage.admin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/health")
    public ResponseEntity health() {
        System.out.println("-------------------------- health 체크 호출");
        return ResponseEntity.ok().build();
    }

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<Void> signupUser(@RequestBody AdminSignupRequest signupRequest) {

        adminService.save(signupRequest);
        return ResponseEntity.ok().build();
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AdminLoginRequest request) {

        adminService.login(request);
        return ResponseEntity.ok().build();
    }

    //일일 판매 기록 조회
    @GetMapping("/summary")
    public ResponseEntity<AdminSummaryResponse> summary(@RequestParam String email) {
        System.out.println("-------------------------- 일일정산 호출");
        AdminSummaryResponse summary = adminService.summary(email);
        return ResponseEntity.ok(summary);
    }

}
