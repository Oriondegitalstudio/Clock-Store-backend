package com.clockstore.Clock_Store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clockstore.Clock_Store.dto.Request.BrandRequest;
import com.clockstore.Clock_Store.dto.Response.BrandResponse;
import com.clockstore.Clock_Store.service.BrandService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<BrandResponse> create(
            @Valid @ModelAttribute BrandRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(brandService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<BrandResponse>> findAll() {

        return ResponseEntity.ok(
                brandService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                brandService.findById(id));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<BrandResponse> update(
            @PathVariable Long id,
            @Valid @ModelAttribute BrandRequest request) {

        return ResponseEntity.ok(
                brandService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        brandService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BrandResponse>> findByStatus(
            @PathVariable boolean status) {

        return ResponseEntity.ok(
                brandService.findByStatus(status));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BrandResponse>> search(
            @RequestParam String name) {

        return ResponseEntity.ok(
                brandService.searchByName(name));
    }
}