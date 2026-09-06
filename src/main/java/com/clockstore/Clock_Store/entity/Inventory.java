package com.clockstore.Clock_Store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false, unique = true)
    private ProductVariant variant;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer reserved;

    @Column(nullable = false)
    private Integer available;

    @Column(nullable = false)
    private Integer lowStockThreshold;

    private String warehouseLocation;

    public Inventory() {
    }

    // Getters & Setters
    public Integer getAvailable() {
        return available;
    }

    public Long getId() {
        return id;
    }

    public Integer getLowStockThreshold() {
        return lowStockThreshold;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getReserved() {
        return reserved;
    }

    public ProductVariant getVariant() {
        return variant;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLowStockThreshold(Integer lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

    public void setVariant(ProductVariant variant) {
        this.variant = variant;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }
}
