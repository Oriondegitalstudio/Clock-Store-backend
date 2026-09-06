package com.clockstore.Clock_Store.dto.Response;

public record ProductOptionValueResponse(
        Long id,
        Long optionId,
        String optionName,
        String value,
        String colorCode,
        String image,
        Integer sortOrder) {
}