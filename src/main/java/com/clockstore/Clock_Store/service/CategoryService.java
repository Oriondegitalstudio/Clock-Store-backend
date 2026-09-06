package com.clockstore.Clock_Store.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clockstore.Clock_Store.dto.Request.CategoryRequest;
import com.clockstore.Clock_Store.dto.Response.CategoryResponse;
import com.clockstore.Clock_Store.entity.Category;
import com.clockstore.Clock_Store.exception.BadRequestException;
import com.clockstore.Clock_Store.exception.DuplicateResourceException;
import com.clockstore.Clock_Store.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public boolean existsBySlug(String slug) {
        return categoryRepository.existsBySlug(slug);
    }

    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Long id) {

        Category category = findCategoryById(id);

        return mapToResponse(category);
    }


    public CategoryResponse createCategory(CategoryRequest request) {

        if (categoryRepository.existsBySlug(request.slug())) {
            throw new DuplicateResourceException(
                    "Category with slug '" + request.slug() + "' already exists"
            );
        }

        Category category = new Category();

        category.setCategoryName(request.name());
        category.setSlug(request.slug());
        category.setDescription(request.description());
        category.setImage(request.image());
        category.setStatus(request.status());
        category.setSortOrder(request.sortOrder());

        if (request.parentId() != null) {

            Category parentCategory =
                    findCategoryById(request.parentId());

            category.setParentCategory(parentCategory);
        }

        Category savedCategory =
                categoryRepository.save(category);

        return mapToResponse(savedCategory);
    }

    public CategoryResponse updateCategory(
            Long id,
            CategoryRequest request) {

        Category existingCategory = findCategoryById(id);

        if (categoryRepository.existsBySlugAndIdNot(
                request.slug(),
                id)) {

            throw new DuplicateResourceException(
                    "Category with slug '" +
                            request.slug() +
                            "' already exists");
        }

        if (request.parentId() != null &&
                request.parentId().equals(id)) {

            throw new BadRequestException(
                    "A category cannot be its own parent");
        }

        existingCategory.setCategoryName(
                request.name());

        existingCategory.setSlug(
                request.slug());

        existingCategory.setDescription(
                request.description());

        existingCategory.setImage(
                request.image());

        existingCategory.setStatus(
                request.status());

        existingCategory.setSortOrder(
                request.sortOrder());

        if (request.parentId() != null) {

            Category parentCategory = findCategoryById(request.parentId());

            existingCategory.setParentCategory(
                    parentCategory);

        } else {

            existingCategory.setParentCategory(null);
        }

        Category savedCategory = categoryRepository.save(existingCategory);

        return mapToResponse(savedCategory);
    }
    public void deleteCategory(Long id) {
        Category category = findCategoryById(id);
        categoryRepository.delete(category);
    }

    public List<CategoryResponse> getCategoriesByParentId(Long parentId) {
        return categoryRepository.findByParentCategoryId(parentId)
            .stream().map(this::mapToResponse).toList();
    }

    public List<CategoryResponse> getActiveCategories() {
        return categoryRepository.findByStatus(true)
            .stream().map(this::mapToResponse).toList();
    }

    public List<CategoryResponse> getCategoriesByStatus(boolean status) {
        return categoryRepository.findByStatus(status)
            .stream().map(this::mapToResponse).toList();
    }

    public List<CategoryResponse> getCategoriesBySortOrder(Integer sortOrder) {
        return categoryRepository.findBySortOrder(sortOrder)
            .stream().map(this::mapToResponse).toList();
    }

    public List<CategoryResponse> getCategoriesByName(String name) {
        return categoryRepository.findByCategoryNameIgnoreCase(name)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Category not found with id: " + id));
    }

    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getParentCategory() != null
                        ? category.getParentCategory().getId()
                        : null,
                category.getCategoryName(),
                category.getSlug(),
                category.getDescription(),
                category.getImage(),
                category.isStatus(),
                category.getSortOrder());
    }
}