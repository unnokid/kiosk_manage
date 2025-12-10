package org.example.kiosk_manage.menu.controller;

import org.example.kiosk_manage.menu.dto.MenuDeleteRequest;
import org.example.kiosk_manage.menu.dto.MenuDto;
import org.example.kiosk_manage.menu.dto.MenuSaveRequest;
import org.example.kiosk_manage.menu.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

    private MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/plus")
    public ResponseEntity<Void> plus(@RequestBody MenuSaveRequest request) {
        menuService.plus(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/minus")
    public ResponseEntity<Void> minus(@RequestBody MenuDeleteRequest request) {
        menuService.minus(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<MenuDto>> allMenuList(@RequestParam("email") String email) {
        List<MenuDto> menuList = menuService.getMenuList(email);

        return ResponseEntity.ok(menuList);
    }


}
