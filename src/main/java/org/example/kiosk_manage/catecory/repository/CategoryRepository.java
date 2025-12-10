package org.example.kiosk_manage.catecory.repository;

import org.example.kiosk_manage.admin.domain.Admin;
import org.example.kiosk_manage.catecory.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {
    List<Category> findCategoriesByAdmin(Admin admin);
}
