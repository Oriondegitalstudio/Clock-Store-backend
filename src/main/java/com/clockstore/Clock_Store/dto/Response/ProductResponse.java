package com.clockstore.Clock_Store.dto.Response;

import java.time.LocalDateTime;
import java.util.List;

import com.clockstore.Clock_Store.entity.enums.ProductStatus;
import com.clockstore.Clock_Store.entity.enums.ProductVisibility;

public record ProductResponse(
                Long id, String name, String slug, String sku, String barcode, Long brandId,
                String brandName, Long categoryId, String categoryName, String shortDescription, String description,
                Double weight, String warranty, ProductStatus status, ProductVisibility visibility, boolean featured,
                LocalDateTime createdAt, LocalDateTime updatedAt, List<String> images) {
}