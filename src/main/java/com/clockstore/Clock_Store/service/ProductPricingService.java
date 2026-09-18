package com.clockstore.Clock_Store.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clockstore.Clock_Store.dto.Request.ProductPricingRequest;
import com.clockstore.Clock_Store.dto.Response.ProductPricingResponse;
import com.clockstore.Clock_Store.entity.ProductPricing;
import com.clockstore.Clock_Store.entity.ProductVariant;
import com.clockstore.Clock_Store.entity.enums.Currency;
import com.clockstore.Clock_Store.repository.ProductPricingRepository;
import com.clockstore.Clock_Store.repository.ProductVariantRepository;

@Service
@Transactional
public class ProductPricingService {

    private final ProductPricingRepository productPricingRepository;
    private final ProductVariantRepository productVariantRepository;

    public ProductPricingService(
            ProductPricingRepository productPricingRepository,
            ProductVariantRepository productVariantRepository) {

        this.productPricingRepository = productPricingRepository;
        this.productVariantRepository = productVariantRepository;
    }

    public ProductPricingResponse create(ProductPricingRequest request) {

        validatePricing(request);

        ProductVariant variant = productVariantRepository.findById(request.variantId())
                .orElseThrow(() -> new RuntimeException(
                        "Product variant not found with id: " + request.variantId()));

        ProductPricing pricing = new ProductPricing();

        pricing.setVariant(variant);
        pricing.setCurrency(request.currency());
        pricing.setPrice(request.price());
        pricing.setSalePrice(request.salePrice());
        pricing.setStartDate(request.startDate());
        pricing.setEndDate(request.endDate());

        ProductPricing savedPricing = productPricingRepository.save(pricing);

        return mapToResponse(savedPricing);
    }

    @Transactional(readOnly = true)
    public ProductPricingResponse getById(Long id) {

        ProductPricing pricing = productPricingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Product pricing not found with id: " + id));

        return mapToResponse(pricing);
    }

    @Transactional(readOnly = true)
    public List<ProductPricingResponse> getByVariantId(Long variantId) {

        if (!productVariantRepository.existsById(variantId)) {
            throw new RuntimeException(
                    "Product variant not found with id: " + variantId);
        }

        return productPricingRepository.findByVariantId(variantId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductPricingResponse> getByVariantIdAndCurrency(
            Long variantId,
            Currency currency) {

        if (!productVariantRepository.existsById(variantId)) {
            throw new RuntimeException(
                    "Product variant not found with id: " + variantId);
        }

        if (currency == null) {
            throw new IllegalArgumentException(
                    "Currency is required");
        }

        return productPricingRepository
                .findByVariantIdAndCurrency(variantId, currency)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductPricingResponse update(
            Long id,
            ProductPricingRequest request) {

        validatePricing(request);

        ProductPricing pricing = productPricingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Product pricing not found with id: " + id));

        ProductVariant variant = productVariantRepository.findById(request.variantId())
                .orElseThrow(() -> new RuntimeException(
                        "Product variant not found with id: " + request.variantId()));

        pricing.setVariant(variant);
        pricing.setCurrency(request.currency());
        pricing.setPrice(request.price());
        pricing.setSalePrice(request.salePrice());
        pricing.setStartDate(request.startDate());
        pricing.setEndDate(request.endDate());

        ProductPricing updatedPricing = productPricingRepository.save(pricing);

        return mapToResponse(updatedPricing);
    }

    public void delete(Long id) {

        if (!productPricingRepository.existsById(id)) {
            throw new RuntimeException(
                    "Product pricing not found with id: " + id);
        }

        productPricingRepository.deleteById(id);
    }

    private void validatePricing(ProductPricingRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Pricing request is required");
        }

        if (request.variantId() == null) {
            throw new IllegalArgumentException(
                    "Variant id is required");
        }

        if (request.currency() == null) {
            throw new IllegalArgumentException(
                    "Currency is required");
        }

        if (request.price() == null) {
            throw new IllegalArgumentException(
                    "Price is required");
        }

        if (request.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Price cannot be negative");
        }

        if (request.salePrice() != null) {

            if (request.salePrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException(
                        "Sale price cannot be negative");
            }

            if (request.salePrice().compareTo(request.price()) > 0) {
                throw new IllegalArgumentException(
                        "Sale price cannot be greater than the regular price");
            }
        }

        if (request.startDate() != null
                && request.endDate() != null
                && request.endDate().isBefore(request.startDate())) {

            throw new IllegalArgumentException(
                    "End date cannot be before start date");
        }
    }

    private ProductPricingResponse mapToResponse(ProductPricing pricing) {

        ProductVariant variant = pricing.getVariant();

        return new ProductPricingResponse(
                pricing.getId(),
                variant.getId(),
                variant.getSku(),
                pricing.getCurrency(),
                pricing.getPrice(),
                pricing.getSalePrice(),
                pricing.getStartDate(),
                pricing.getEndDate());
    }
}