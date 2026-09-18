package com.clockstore.Clock_Store.dto.Response;

import com.clockstore.Clock_Store.entity.enums.VariantStatus;

public record ProductVariantResponse(

        Long id,

        Long productId,

        String productName,

        String sku,

        String barcode,

        Double weight,

        VariantStatus status

) {
}