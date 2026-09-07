package com.clockstore.Clock_Store.dto.Request;

import com.clockstore.Clock_Store.entity.enums.VariantStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class ProductVariantRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "SKU is required")
    @Size(max = 255, message = "SKU must not exceed 255 characters")
    private String sku;

    @Size(max = 255, message = "Barcode must not exceed 255 characters")
    private String barcode;

    @PositiveOrZero(message = "Weight must be zero or positive")
    private Double weight;

    @NotNull(message = "Variant status is required")
    private VariantStatus status;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public VariantStatus getStatus() {
        return status;
    }

    public void setStatus(VariantStatus status) {
        this.status = status;
    }
}