package com.clockstore.Clock_Store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clockstore.Clock_Store.dto.Request.ProductPricingRequest;
import com.clockstore.Clock_Store.dto.Response.ProductPricingResponse;
import com.clockstore.Clock_Store.entity.enums.Currency;
import com.clockstore.Clock_Store.service.ProductPricingService;

@RestController
@RequestMapping("/api/product-pricing")
public class ProductPricingController {

    private final ProductPricingService productPricingService;

    public ProductPricingController(
            ProductPricingService productPricingService) {

        this.productPricingService = productPricingService;
    }

    @PostMapping
    public ResponseEntity<ProductPricingResponse> create(
            @RequestBody ProductPricingRequest request) {

        ProductPricingResponse response = productPricingService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductPricingResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productPricingService.getById(id));
    }

    @GetMapping("/variant/{variantId}")
    public ResponseEntity<List<ProductPricingResponse>> getByVariantId(
            @PathVariable Long variantId) {

        return ResponseEntity.ok(
                productPricingService.getByVariantId(variantId));
    }

    @GetMapping("/variant/{variantId}/currency")
    public ResponseEntity<List<ProductPricingResponse>> getByVariantIdAndCurrency(
            @PathVariable Long variantId,
            @RequestParam Currency currency) {

        return ResponseEntity.ok(
                productPricingService.getByVariantIdAndCurrency(
                        variantId,
                        currency));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductPricingResponse> update(
            @PathVariable Long id,
            @RequestBody ProductPricingRequest request) {

        return ResponseEntity.ok(
                productPricingService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        productPricingService.delete(id);

        return ResponseEntity.noContent().build();
    }
}