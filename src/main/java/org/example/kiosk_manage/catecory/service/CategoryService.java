package org.example.kiosk_manage.catecory.service;


import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.admin.repository.AdminRepository;
import org.example.kiosk_manage.catecory.domain.Category;
import org.example.kiosk_manage.catecory.dto.CategoryDeleteRequest;
import org.example.kiosk_manage.catecory.dto.CategoryDto;
import org.example.kiosk_manage.catecory.dto.CategoryListRequest;
import org.example.kiosk_manage.catecory.dto.CategorySaveRequest;
import org.example.kiosk_manage.catecory.repository.CategoryRepository;
import org.example.kiosk_manage.common.exception.BadRequestException;
import org.example.kiosk_manage.donation.domain.Donation;
import org.example.kiosk_manage.menu.domain.Menu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AdminRepository adminRepository;

    public CategoryService(CategoryRepository categoryRepository, AdminRepository adminRepository) {
        this.categoryRepository = categoryRepository;
        this.adminRepository = adminRepository;
    }

    public List<CategoryDto> getAllCategories(String email) {

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("해당 아이디를 가진 고객이 존재하지 않습니다."));

        return categoryRepository.findCategoriesByAdminAndActiveTrue(admin)
                .stream()
                .map(category -> CategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName()).build())
                .collect(Collectors.toList());

    }

    public void plus(CategorySaveRequest request) {
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("해당 아이디를 가진 고객이 존재하지 않습니다."));

        boolean check = admin.getCategoryList().stream()
                .filter(Category::isActive)
                .anyMatch(category -> category.getName().equals(request.getName()));

        if (check) {
            throw new BadRequestException("요청하신 카테고리는 이미 존재 합니다. 다시 요청 해주세요.");
        }

        categoryRepository.save(Category.builder()
                .name(request.getName())
                .active(true)
                .admin(admin)
                .build());
    }

    @Transactional
    public void minus(CategoryDeleteRequest request) {

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("해당 아이디를 가진 고객이 존재하지 않습니다."));

        Category category = admin.getCategoryList().stream()
                .filter(o -> o.getId().equals(request.getCategoryId()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("해당 카테고리는 존재하지 않습니다."));

        boolean hasActiveMenus = category.getMenuList().stream()
                .anyMatch(Menu::isActive);
        if (hasActiveMenus) {
            throw new IllegalStateException("삭제 불가: 메뉴가 존재합니다.");
        }

        category.deactivate();

        categoryRepository.save(category);
    }
}
