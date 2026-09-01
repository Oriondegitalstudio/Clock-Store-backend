package com.clockstore.Clock_Store.dto.Request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        Long parentId,
        @NotBlank(message = "Category name is required") String name,
        @NotBlank(message = "Slug is required") String slug,
        String description,
        String image,
        boolean status,
        Integer sortOrder
) {}