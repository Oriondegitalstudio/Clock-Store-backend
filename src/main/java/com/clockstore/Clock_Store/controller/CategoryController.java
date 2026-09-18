package com.clockstore.Clock_Store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clockstore.Clock_Store.dto.Request.CategoryRequest;
import com.clockstore.Clock_Store.dto.Response.CategoryResponse;
import com.clockstore.Clock_Store.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {

        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponse>> getActiveCategories() {
        return ResponseEntity.ok(categoryService.getActiveCategories());
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByParentId(@PathVariable Long parentId) {
        return ResponseEntity.ok(categoryService.getCategoriesByParentId(parentId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByStatus(@PathVariable boolean status) {
        return ResponseEntity.ok(categoryService.getCategoriesByStatus(status));
    }

    @GetMapping("/sort-order/{sortOrder}")
    public ResponseEntity<List<CategoryResponse>> getCategoriesBySortOrder(@PathVariable Integer sortOrder) {
        return ResponseEntity.ok(categoryService.getCategoriesBySortOrder(sortOrder));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByName(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.getCategoriesByName(name));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsBySlug(@RequestParam String slug) {
        return ResponseEntity.ok(categoryService.existsBySlug(slug));
    }
}