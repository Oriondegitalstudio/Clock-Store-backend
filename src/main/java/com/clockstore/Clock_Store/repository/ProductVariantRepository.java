package com.clockstore.Clock_Store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clockstore.Clock_Store.entity.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    List<ProductVariant> findByProductId(Long productId);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);
}