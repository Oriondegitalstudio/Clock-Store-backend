package com.clockstore.Clock_Store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clockstore.Clock_Store.entity.ProductPricing;
import com.clockstore.Clock_Store.entity.enums.Currency;

public interface ProductPricingRepository extends JpaRepository<ProductPricing, Long> {

    List<ProductPricing> findByVariantId(Long variantId);

    List<ProductPricing> findByVariantIdAndCurrency(
            Long variantId,
            Currency currency);
}