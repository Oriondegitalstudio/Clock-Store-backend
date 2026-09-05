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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clockstore.Clock_Store.dto.Request.ProductRequest;
import com.clockstore.Clock_Store.dto.Response.ProductResponse;
import com.clockstore.Clock_Store.entity.enums.ProductStatus;
import com.clockstore.Clock_Store.entity.enums.ProductVisibility;
import com.clockstore.Clock_Store.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

        private final ProductService productService;

        public ProductController(ProductService productService) {
                this.productService = productService;
        }

        @PostMapping(consumes = "multipart/form-data")
        public ResponseEntity<ProductResponse> create(
                        @Valid @ModelAttribute @RequestBody ProductRequest request) {

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(productService.create(request));
        }

        @GetMapping
        public ResponseEntity<List<ProductResponse>> findAll() {

                return ResponseEntity.ok(
                                productService.findAll());
        }

        @GetMapping("/{id}")
        public ResponseEntity<ProductResponse> findById(
                        @PathVariable Long id) {

                return ResponseEntity.ok(
                                productService.findById(id));
        }

        @PutMapping(value = "/{id}", consumes = "multipart/form-data")
        public ResponseEntity<ProductResponse> update(
                        @PathVariable Long id,
                        @Valid @ModelAttribute ProductRequest request) {

                return ResponseEntity.ok(
                                productService.update(id, request));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(
                        @PathVariable Long id) {

                productService.delete(id);

                return ResponseEntity.noContent().build();
        }

        @GetMapping("/status/{status}")
        public ResponseEntity<List<ProductResponse>> findByStatus(
                        @PathVariable ProductStatus status) {

                return ResponseEntity.ok(
                                productService.findByStatus(status));
        }

        @GetMapping("/visibility/{visibility}")
        public ResponseEntity<List<ProductResponse>> findByVisibility(
                        @PathVariable ProductVisibility visibility) {

                return ResponseEntity.ok(
                                productService.findByVisibility(visibility));
        }

        @GetMapping("/featured")
        public ResponseEntity<List<ProductResponse>> findFeatured() {

                return ResponseEntity.ok(
                                productService.findFeatured());
        }

        @GetMapping("/search")
        public ResponseEntity<List<ProductResponse>> search(
                        @RequestParam String name) {

                return ResponseEntity.ok(
                                productService.searchByName(name));
        }
}