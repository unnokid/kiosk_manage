package org.example.kiosk_manage.admin.service;

import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.admin.dto.AdminLoginRequest;
import org.example.kiosk_manage.admin.dto.AdminSignupRequest;
import org.example.kiosk_manage.admin.dto.AdminSummaryResponse;
import org.example.kiosk_manage.admin.repository.AdminRepository;
import org.example.kiosk_manage.cart.domain.Cart;
import org.example.kiosk_manage.common.Validation;
import org.example.kiosk_manage.common.exception.BadRequestException;
import org.example.kiosk_manage.donation.domain.Donation;
import org.example.kiosk_manage.order.domain.Order;
import org.example.kiosk_manage.order.domain.Payment;
import org.example.kiosk_manage.order.dto.OrderMenuDto;
import org.example.kiosk_manage.order.dto.OrderSummaryDto;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public void save(AdminSignupRequest request) {

        //이메일 & 비밀번호 체크
        Validation.validateEmail(request.getEmail());
        Validation.validatePassword(request.getPassword());
        Validation.matchPassword(request.getPassword(), request.getPasswordCheck());

        if (adminRepository.existsAdminByEmail(request.getEmail())) {
            throw new BadRequestException("요청하신 이메일 존재 합니다. 다시 요청 해주세요.");
        }

        adminRepository.save(Admin.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .build());
    }

    public void login(AdminLoginRequest request) {
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("아이디가 잘못 입력되었습니다."));
        if (!admin.matchPassword(request.getPassword())) {
            throw new BadRequestException("아이디가 잘못 입력되었습니다.");
        }
    }

    public AdminSummaryResponse summary(String email) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("아이디가 잘못 입력되었습니다."));

        List<Donation> donationList = admin.getDonationList();
        int totalDonationCount = donationList.size();
        int totalDonationAmount = donationList.stream()
                .mapToInt(Donation::getAmount).sum();

        List<Order> orderEntityList = admin.getOrderList();
        int totalOrderCount = orderEntityList.size();
        int totalOrderPrice = orderEntityList.stream()
                .mapToInt(order -> Math.max(order.getTotal_amount(), order.getPaidAmount()))
                .sum();

        Map<Payment, Long> paymentAmountMap = orderEntityList.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getPayment() != null ? o.getPayment() : Payment.ETC, // null 방어
                        Collectors.summingLong(o -> Math.max(o.getTotal_amount(), o.getPaidAmount()))
                ));

        // (선택) 결제방식별 건수도 같이
        Map<Payment, Long> paymentCountMap = orderEntityList.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getPayment() != null ? o.getPayment() : Payment.ETC,
                        Collectors.counting()
                ));


        List<OrderSummaryDto> orderList = orderEntityList.stream()
                .map(order -> {
                    List<OrderMenuDto> menuList = order.getCartList().stream()
                            .map(cart -> {
                                String menuName = cart.getMenu().getName();
                                String optionName = null;
                                if (cart.getCartOptionList() != null
                                        && !cart.getCartOptionList().isEmpty()
                                        && cart.getCartOptionList().get(0) != null
                                        && cart.getCartOptionList().get(0).getMenuOption() != null) {
                                    optionName = cart.getCartOptionList()
                                            .get(0)
                                            .getMenuOption()
                                            .getOptionName();
                                }
                                return new OrderMenuDto(
                                        menuName,
                                        optionName,
                                        cart.getQuantity()
                                );
                            })
                            .toList();

                    return new OrderSummaryDto(
                            order.getOrderNo(),
                            order.getCreateDate().toString(),
                            order.getStatus(),
                            order.getTotal_amount(),
                            order.getPaidAmount(),
                            menuList
                    );
                })
                .toList();


        Map<String, Long> result = admin.getOrderList().stream()
                .flatMap(order -> order.getCartList().stream())
                .collect(Collectors.groupingBy(
                        cart -> {
                            String menuName = cart.getMenu().getName();
                            String optionName = cart.getCartOptionList().get(0) != null
                                    ? cart.getCartOptionList().get(0).getMenuOption().getOptionName()
                                    : "";
                            return optionName.isEmpty()
                                    ? menuName
                                    : menuName + " (" + optionName + ")";
                        },
                        Collectors.summingLong(Cart::getQuantity)
                ))
                .entrySet().stream()
                .sorted(
                        Map.Entry.<String, Long>comparingByValue().reversed()
                                .thenComparing(Map.Entry.comparingByKey())   // 수량 같으면 이름순
                )
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));


        return
                AdminSummaryResponse
                        .builder()
                        .totalCount(totalDonationCount + totalOrderCount)
                        .totalAmount(totalDonationAmount + totalOrderPrice)
                        .paymentCount(paymentCountMap)
                        .paymentAmount(paymentAmountMap)
                        .totalDonationCount(totalDonationCount)
                        .totalDonationAmount(totalDonationAmount)
                        .donationList(donationList)
                        .totalOrderCount(totalOrderCount)
                        .totalOrderPrice(totalOrderPrice)
                        .orderList(orderList)
                        .menuCountMap(result)
                        .build();

    }
}
