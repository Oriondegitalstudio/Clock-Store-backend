package com.clockstore.Clock_Store.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_pricing")
public class ProductPricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal price;

    @Column(precision = 12, scale = 3)
    private BigDecimal salePrice;

    private LocalDate startDate;

    private LocalDate endDate;

    public ProductPricing() {
    }

    // Getters & Setters
    public String getCurrency() {
        return currency;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public ProductVariant getVariant() {
        return variant;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setVariant(ProductVariant variant) {
        this.variant = variant;
    }
}