package com.clockstore.Clock_Store.entity;

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
@Table(name = "product_option_values")
public class ProductOptionValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_id", nullable = false)
    private ProductOption option;

    @Column(nullable = false)
    private String value;

    private String colorCode;

    private String image;

    private Integer sortOrder;

    public ProductOptionValue() {
    }

    // Getters & Setters
    public String getColorCode() {
        return colorCode;
    }

    public Long getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public ProductOption getOption() {
        return option;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public String getValue() {
        return value;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setOption(ProductOption option) {
        this.option = option;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
