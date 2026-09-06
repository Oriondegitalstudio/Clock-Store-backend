package com.clockstore.Clock_Store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clockstore.Clock_Store.dto.Request.ProductOptionValueRequest;
import com.clockstore.Clock_Store.dto.Response.ProductOptionValueResponse;
import com.clockstore.Clock_Store.service.ProductOptionValueService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/product-option-values")
public class ProductOptionValueController {

    private final ProductOptionValueService productOptionValueService;

    public ProductOptionValueController(
            ProductOptionValueService productOptionValueService) {

        this.productOptionValueService = productOptionValueService;
    }

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductOptionValueResponse create(
            @Valid @ModelAttribute ProductOptionValueRequest request) {

        return productOptionValueService.create(request);
    }

    @GetMapping
    public List<ProductOptionValueResponse> findAll() {

        return productOptionValueService.findAll();
    }

    @GetMapping("/{id}")
    public ProductOptionValueResponse findById(
            @PathVariable Long id) {

        return productOptionValueService.findById(id);
    }

    @GetMapping("/option/{optionId}")
    public List<ProductOptionValueResponse> findByOptionId(
            @PathVariable Long optionId) {

        return productOptionValueService.findByOptionId(optionId);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ProductOptionValueResponse update(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductOptionValueRequest request) {

        return productOptionValueService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        productOptionValueService.delete(id);
    }
}