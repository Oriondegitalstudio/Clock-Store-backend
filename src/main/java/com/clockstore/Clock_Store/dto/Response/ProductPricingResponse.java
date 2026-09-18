package com.clockstore.Clock_Store.dto.Response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.clockstore.Clock_Store.entity.enums.Currency;

public record ProductPricingResponse(

        Long id,

        Long variantId,

        String variantSku,

        Currency currency,

        BigDecimal price,

        BigDecimal salePrice,

        LocalDate startDate,

        LocalDate endDate

) {
}