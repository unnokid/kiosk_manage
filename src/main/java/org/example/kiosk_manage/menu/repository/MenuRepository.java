package org.example.kiosk_manage.menu.repository;

import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.catecory.domain.Category;
import org.example.kiosk_manage.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu,Long>, MenuRepositoryCustom {
    List<Menu> findMenusByCategory(Category category);
}
