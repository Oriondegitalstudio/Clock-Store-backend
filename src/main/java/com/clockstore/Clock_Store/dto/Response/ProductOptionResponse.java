package com.clockstore.Clock_Store.dto.Response;

import com.clockstore.Clock_Store.entity.enums.OptionDisplayType;

public record ProductOptionResponse(
        Long id,
        Long productId,
        String productName,
        String name,
        OptionDisplayType displayType,
        Integer sortOrder) {
}