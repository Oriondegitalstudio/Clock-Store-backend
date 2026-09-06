package com.clockstore.Clock_Store.dto.Request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.clockstore.Clock_Store.entity.enums.ProductStatus;
import com.clockstore.Clock_Store.entity.enums.ProductVisibility;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequest(
                @NotBlank(message = "Product name is required") @Size(max = 255, message = "Product name must not exceed 255 characters") String name,

                @NotBlank(message = "Product slug is required") @Size(max = 255, message = "Product slug must not exceed 255 characters") String slug,

                @NotBlank(message = "Product SKU is required") @Size(max = 255, message = "Product SKU must not exceed 255 characters") String sku,
                @Size(max = 255, message = "Barcode must not exceed 255 characters") String barcode,
                @NotNull(message = "Brand ID is required") Long brandId,
                @NotNull(message = "Category ID is required") Long categoryId, String shortDescription,
                @NotBlank(message = "Product description is required") String description, Double weight,
                String warranty, @NotNull(message = "Product status is required") ProductStatus status,
                @NotNull(message = "Product visibility is required") ProductVisibility visibility,
                @NotNull(message = "Featured status is required") Boolean featured, List<MultipartFile> images) {
}