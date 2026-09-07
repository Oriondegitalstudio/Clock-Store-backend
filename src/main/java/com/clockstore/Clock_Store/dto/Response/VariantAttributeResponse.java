package com.clockstore.Clock_Store.dto.Response;

public record VariantAttributeResponse(

        Long id,

        Long variantId,

        String variantSku,

        Long optionId,

        String optionName,

        Long optionValueId,

        String optionValue

) {
}