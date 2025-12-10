package org.example.kiosk_manage.catecory.controller;

import lombok.Getter;
import org.example.kiosk_manage.admin.dto.AdminLoginRequest;
import org.example.kiosk_manage.catecory.dto.CategoryDeleteRequest;
import org.example.kiosk_manage.catecory.dto.CategoryDto;
import org.example.kiosk_manage.catecory.dto.CategoryListRequest;
import org.example.kiosk_manage.catecory.dto.CategorySaveRequest;
import org.example.kiosk_manage.catecory.service.CategoryService;
import org.example.kiosk_manage.donation.dto.DonationDeleteRequest;
import org.example.kiosk_manage.donation.dto.DonationSaveRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<CategoryDto>> getAllCategories(@RequestBody CategoryListRequest request) {
        List<CategoryDto> allCategories = categoryService.getAllCategories(request);

        return ResponseEntity.ok(allCategories);
    }

    @PostMapping("/plus")
    public ResponseEntity<Void> plus(@RequestBody CategorySaveRequest request) {
        categoryService.plus(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/minus")
    public ResponseEntity<Void> minus(@RequestBody CategoryDeleteRequest request) {
        categoryService.minus(request);

        return ResponseEntity.ok().build();
    }

}
