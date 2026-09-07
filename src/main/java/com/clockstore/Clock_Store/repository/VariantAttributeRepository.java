package com.clockstore.Clock_Store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clockstore.Clock_Store.entity.VariantAttribute;

public interface VariantAttributeRepository extends JpaRepository<VariantAttribute, Long> {

    List<VariantAttribute> findByVariantId(Long variantId);

    boolean existsByVariantIdAndOptionId(Long variantId, Long optionId);

    boolean existsByVariantIdAndOptionIdAndIdNot(
            Long variantId,
            Long optionId,
            Long id);
}