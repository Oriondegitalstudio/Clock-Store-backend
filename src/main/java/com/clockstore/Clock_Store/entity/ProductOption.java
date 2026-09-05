package com.clockstore.Clock_Store.entity;
import com.clockstore.Clock_Store.entity.enums.OptionDisplayType;

import jakarta.persistence.*;

@Entity
@Table(name = "product_options")
public class ProductOption {
        @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OptionDisplayType displayType;

    private Integer sortOrder;

    public ProductOption() {
    }

    // Getters & Setters
    public OptionDisplayType getDisplayType() {
        return displayType;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Products getProduct() {
        return product;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setDisplayType(OptionDisplayType displayType) {
        this.displayType = displayType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProduct(Products product) {
        this.product = product;
    }
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

}
