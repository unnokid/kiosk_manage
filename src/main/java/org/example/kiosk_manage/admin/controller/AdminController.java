package org.example.kiosk_manage.admin.controller;

import org.example.kiosk_manage.admin.dto.*;
import org.example.kiosk_manage.admin.service.AdminService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
    public ResponseEntity<AdminSummaryResponse> mainSummary(@RequestParam String email,  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMdd")LocalDate date) {
        System.out.println("-------------------------- 총 일일정산 호출");

        LocalDate targetDate = (date != null) ? date : LocalDate.now(); // 기본 오늘
        AdminSummaryResponse summary = adminService.summary(email, targetDate);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/order")
    public ResponseEntity<AdminOrderSummaryResponse> orderSummary(@RequestParam String email, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMdd")LocalDate date) {
        System.out.println("-------------------------- 주문 일일정산 호출");

        LocalDate targetDate = (date != null) ? date : LocalDate.now(); // 기본 오늘
        AdminOrderSummaryResponse summary = adminService.orderSummary(email, targetDate);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/donation")
    public ResponseEntity<AdminDonationSummaryResponse> donationSummary(@RequestParam String email, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMdd")LocalDate date) {
        System.out.println("-------------------------- 찬조 일일정산 호출");

        LocalDate targetDate = (date != null) ? date : LocalDate.now(); // 기본 오늘
        AdminDonationSummaryResponse summary = adminService.donationSummary(email, targetDate);

        return ResponseEntity.ok(summary);
    }

}
