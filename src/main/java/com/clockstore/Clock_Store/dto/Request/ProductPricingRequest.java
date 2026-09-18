package com.clockstore.Clock_Store.dto.Request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.clockstore.Clock_Store.entity.enums.Currency;

public record ProductPricingRequest(

        Long variantId,

        Currency currency,

        BigDecimal price,

        BigDecimal salePrice,

        LocalDate startDate,

        LocalDate endDate

) {
}