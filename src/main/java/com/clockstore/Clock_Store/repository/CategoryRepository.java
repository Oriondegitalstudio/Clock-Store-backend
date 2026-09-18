package com.clockstore.Clock_Store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clockstore.Clock_Store.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Custom query methods can be defined here if needed
    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    List<Category> findByStatus(boolean status);

    List<Category> findByParentCategoryId(Long parentId);

    List<Category> findBySortOrder(Integer sortOrder);

    List<Category> findByCategoryNameIgnoreCase(String name);
}