package com.clockstore.Clock_Store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clockstore.Clock_Store.dto.Request.VariantAttributeRequest;
import com.clockstore.Clock_Store.dto.Response.VariantAttributeResponse;
import com.clockstore.Clock_Store.service.VariantAttributeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/variant-attributes")
public class VariantAttributeController {

    private final VariantAttributeService variantAttributeService;

    public VariantAttributeController(
            VariantAttributeService variantAttributeService) {

        this.variantAttributeService = variantAttributeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VariantAttributeResponse create(
            @Valid @RequestBody VariantAttributeRequest request) {

        return variantAttributeService.create(request);
    }

    @GetMapping
    public List<VariantAttributeResponse> findAll() {

        return variantAttributeService.findAll();
    }

    @GetMapping("/{id}")
    public VariantAttributeResponse findById(
            @PathVariable Long id) {

        return variantAttributeService.findById(id);
    }

    @GetMapping("/variant/{variantId}")
    public List<VariantAttributeResponse> findByVariantId(
            @PathVariable Long variantId) {

        return variantAttributeService.findByVariantId(variantId);
    }

    @PutMapping("/{id}")
    public VariantAttributeResponse update(
            @PathVariable Long id,
            @Valid @RequestBody VariantAttributeRequest request) {

        return variantAttributeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        variantAttributeService.delete(id);
    }
}