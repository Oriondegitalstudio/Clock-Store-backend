package com.clockstore.Clock_Store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clockstore.Clock_Store.entity.ProductOption;

public interface ProductOptionRepository
        extends JpaRepository<ProductOption, Long> {

    List<ProductOption> findByProductIdOrderBySortOrderAsc(Long productId);

    List<ProductOption> findByNameContainingIgnoreCase(String name);

}