package com.clockstore.Clock_Store.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @Column(nullable = false)
    private String url;//file upload
    private String altText;

    private Integer sortOrder;

    @Column(nullable = false)
    private boolean isPrimary;

    public ProductImage() {
    }

    // Getters & Setters
    public String getAltText() {
        return altText;
    }

    public Long getId() {
        return id;
    }

    public Products getProduct() {
        return product;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public String getUrl() {
        return url;
    }

    public ProductVariant getVariant() {
        return variant;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public void setVariant(ProductVariant variant) {
        this.variant = variant;
    }
}