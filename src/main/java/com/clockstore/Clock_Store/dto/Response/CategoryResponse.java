package com.clockstore.Clock_Store.dto.Response;

public record CategoryResponse(
        Long id,
        Long parentId,
        String name,
        String slug,
        String description,
        String image,
        boolean status,
        Integer sortOrder) 
{}