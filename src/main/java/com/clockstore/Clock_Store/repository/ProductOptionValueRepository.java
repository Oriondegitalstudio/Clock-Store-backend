package com.clockstore.Clock_Store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clockstore.Clock_Store.entity.ProductOptionValue;

public interface ProductOptionValueRepository extends JpaRepository<ProductOptionValue, Long> {

    List<ProductOptionValue> findByOptionIdOrderBySortOrderAsc(Long optionId);
}