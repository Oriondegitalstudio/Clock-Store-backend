package com.clockstore.Clock_Store.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "variant_attributes")
public class VariantAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_id", nullable = false)
    private ProductOption option;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_value_id", nullable = false)
    private ProductOptionValue optionValue;

    public VariantAttribute() {
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public ProductOption getOption() {
        return option;
    }

    public ProductOptionValue getOptionValue() {
        return optionValue;
    }

    public ProductVariant getVariant() {
        return variant;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOption(ProductOption option) {
        this.option = option;
    }

    public void setOptionValue(ProductOptionValue optionValue) {
        this.optionValue = optionValue;
    }

    public void setVariant(ProductVariant variant) {
        this.variant = variant;
    }
}