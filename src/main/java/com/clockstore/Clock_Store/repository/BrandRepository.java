package com.clockstore.Clock_Store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clockstore.Clock_Store.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Optional<Brand> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<Brand> findByStatus(boolean status);

    List<Brand> findByNameContainingIgnoreCase(String name);
}