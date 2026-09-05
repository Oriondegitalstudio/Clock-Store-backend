package com.clockstore.Clock_Store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clockstore.Clock_Store.entity.Products;
import com.clockstore.Clock_Store.entity.enums.ProductStatus;
import com.clockstore.Clock_Store.entity.enums.ProductVisibility;

public interface ProductRepository extends JpaRepository<Products, Long> {

    boolean existsBySlugIgnoreCase(String slug);

    boolean existsBySkuIgnoreCase(String sku);

    Optional<Products> findBySlugIgnoreCase(String slug);

    List<Products> findByStatus(ProductStatus status);

    List<Products> findByVisibility(ProductVisibility visibility);

    List<Products> findByFeaturedTrue();

    List<Products> findByNameContainingIgnoreCase(String name);
}