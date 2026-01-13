package org.example.kiosk_manage.menu.service;


import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.admin.repository.AdminRepository;
import org.example.kiosk_manage.catecory.domain.Category;
import org.example.kiosk_manage.catecory.repository.CategoryRepository;
import org.example.kiosk_manage.common.converter.Converter;
import org.example.kiosk_manage.common.exception.BadRequestException;
import org.example.kiosk_manage.menu.domain.Menu;
import org.example.kiosk_manage.menu.domain.MenuOption;
import org.example.kiosk_manage.menu.dto.*;
import org.example.kiosk_manage.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class MenuService {

    private final AdminRepository adminRepository;

    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;


    public MenuService(
            AdminRepository adminRepository,
            CategoryRepository categoryRepository,
            MenuRepository menuRepository
    ) {
        this.adminRepository = adminRepository;
        this.categoryRepository = categoryRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public void plus(MenuSaveRequest request) {

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("해당 아이디를 가진 고객이 존재하지 않습니다."));


        boolean check = admin.getCategoryList().stream()
                .flatMap(category -> category.getMenuList().stream())
                .anyMatch(menu -> menu.getName().equals(request.getName()));

        //메뉴중복 체크
        if (check) {
            throw new BadRequestException("요청하신 메뉴는 이미 존재 합니다. 다시 요청 해주세요.");
        }

        //카테고리 연동
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BadRequestException("요청하신 카테고리 값이 존재하지 않습니다."));

        //메뉴 객체 생성
        Menu menu = Menu.builder()
                .name(request.getName())
                .price(request.getPrice())
                .remain(request.getCount())
                .active(true)
                .category(category)
                .menuOptionList(new ArrayList<>())
                .build();

        //요청된 메뉴 옵션 연동
        for (MenuOptionSaveRequest m : request.getOptions()) {
            MenuOption menuOption = MenuOption
                    .builder()
                    .optionName(m.getOption())
                    .optionPrice(m.getOptionPrice())
                    .active(true)
                    .menu(menu)
                    .build();
            menu.addMenuOption(menuOption);
        }

        //저장

        menuRepository.save(menu);
    }

    @Transactional
    public void minus(MenuDeleteRequest request) {

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("해당 아이디를 가진 고객이 존재하지 않습니다."));

        Menu target = admin.getCategoryList().stream()
                .flatMap(category -> category.getMenuList().stream())
                .filter(menu -> menu.getId().equals(request.getMenuId()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("삭제요청된 메뉴는 존재하지 않습니다."));

        //주문이 존재할 수 있기에 soft delete
        target.deactivate();

        target.getMenuOptionList().forEach(MenuOption::deactivate);

        menuRepository.save(target);

    }

    public List<MenuDto> getMenuList(String email) {

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("해당 아이디를 가진 고객이 존재하지 않습니다."));

        List<Category> categories = categoryRepository.findCategoriesByAdminAndActiveTrue(admin);

        List<MenuDto> menuDtoList = new ArrayList<>();
        for (Category category : categories){
            List<Menu> menuList = menuRepository.findMenusByCategoryAndActiveTrue(category);

            menuDtoList.addAll(
                menuList.stream()
                        .map(menu ->
                            MenuDto.builder()
                                    .id(menu.getId())
                                    .name(menu.getName())
                                    .category(menu.getCategory().getName())
                                    .price(menu.getPrice())
                                    .options( Converter.toMenuOptionDtos(
                                            menu.getMenuOptionList().stream()
                                                    .filter(MenuOption::isActive)
                                                    .toList()
                                    ))
                                    .build()
                        ).toList()
            );
        }

        return menuDtoList;
    }
}
