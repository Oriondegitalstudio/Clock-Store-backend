package com.clockstore.Clock_Store.entity;

import com.clockstore.Clock_Store.entity.enums.VariantStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @Column(nullable = false, unique = true)
    private String sku;

    private String barcode;

    private Double weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VariantStatus status;

    public ProductVariant() {
    }

    // Getters & Setters
    public String getBarcode() {
        return barcode;
    }

    public Long getId() {
        return id;
    }

    public Products getProduct() {
        return product;
    }

    public String getSku() {
        return sku;
    }

    public VariantStatus getStatus() {
        return status;
    }

    public Double getWeight() {
        return weight;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setStatus(VariantStatus status) {
        this.status = status;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

}