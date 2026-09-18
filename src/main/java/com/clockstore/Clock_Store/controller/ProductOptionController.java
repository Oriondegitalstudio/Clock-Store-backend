package com.clockstore.Clock_Store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clockstore.Clock_Store.dto.Request.ProductOptionRequest;
import com.clockstore.Clock_Store.dto.Response.ProductOptionResponse;
import com.clockstore.Clock_Store.service.ProductOptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/product-options")
public class ProductOptionController {

    private final ProductOptionService productOptionService;

    public ProductOptionController(
            ProductOptionService productOptionService) {

        this.productOptionService = productOptionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductOptionResponse create(
            @Valid @RequestBody ProductOptionRequest request) {

        return productOptionService.create(request);
    }

    @GetMapping
    public List<ProductOptionResponse> findAll() {

        return productOptionService.findAll();
    }

    @GetMapping("/{id}")
    public ProductOptionResponse findById(
            @PathVariable Long id) {

        return productOptionService.findById(id);
    }

    @GetMapping("/product/{productId}")
    public List<ProductOptionResponse> findByProductId(
            @PathVariable Long productId) {

        return productOptionService.findByProductId(productId);
    }

    @PutMapping("/{id}")
    public ProductOptionResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ProductOptionRequest request) {

        return productOptionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id) {

        productOptionService.delete(id);
    }
}