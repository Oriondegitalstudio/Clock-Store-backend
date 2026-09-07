package com.clockstore.Clock_Store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clockstore.Clock_Store.dto.Request.ProductVariantRequest;
import com.clockstore.Clock_Store.dto.Response.ProductVariantResponse;
import com.clockstore.Clock_Store.service.ProductVariantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/product-variants")
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    public ProductVariantController(
            ProductVariantService productVariantService) {

        this.productVariantService = productVariantService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductVariantResponse create(
            @Valid @RequestBody ProductVariantRequest request) {

        return productVariantService.create(request);
    }

    @GetMapping
    public List<ProductVariantResponse> findAll() {

        return productVariantService.findAll();
    }

    @GetMapping("/{id}")
    public ProductVariantResponse findById(
            @PathVariable Long id) {

        return productVariantService.findById(id);
    }

    @GetMapping("/product/{productId}")
    public List<ProductVariantResponse> findByProductId(
            @PathVariable Long productId) {

        return productVariantService.findByProductId(productId);
    }

    @PutMapping("/{id}")
    public ProductVariantResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ProductVariantRequest request) {

        return productVariantService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id) {

        productVariantService.delete(id);
    }
}