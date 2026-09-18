package com.clockstore.Clock_Store.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clockstore.Clock_Store.dto.Request.BrandRequest;
import com.clockstore.Clock_Store.dto.Response.BrandResponse;
import com.clockstore.Clock_Store.entity.Brand;
import com.clockstore.Clock_Store.exception.DuplicateResourceException;
import com.clockstore.Clock_Store.exception.ResourceNotFoundException;
import com.clockstore.Clock_Store.repository.BrandRepository;

@Service
@Transactional
public class BrandService {

    private final BrandRepository brandRepository;
    private final FileStorageService fileStorageService;

    public BrandService(
            BrandRepository brandRepository,
            FileStorageService fileStorageService) {

        this.brandRepository = brandRepository;
        this.fileStorageService = fileStorageService;
    }

    public BrandResponse create(BrandRequest request) {

        if (brandRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException(
                    "Brand with name '" + request.name() + "' already exists");
        }

        Brand brand = new Brand();

        brand.setName(request.name());
        brand.setDescription(request.description());
        brand.setWebsite(request.website());
        brand.setStatus(
                request.status() != null
                        ? request.status()
                        : true);

        if (request.logo() != null && !request.logo().isEmpty()) {
            String logoUrl = fileStorageService.store(
                    request.logo(),
                    "brands");

            brand.setLogo(logoUrl);
        }

        Brand savedBrand = brandRepository.save(brand);

        return toResponse(savedBrand);
    }

    @Transactional(readOnly = true)
    public List<BrandResponse> findAll() {

        return brandRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BrandResponse findById(Long id) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Brand with id " + id + " not found"));

        return toResponse(brand);
    }

    public BrandResponse update(Long id, BrandRequest request) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Brand with id " + id + " not found"));

        if (!brand.getName().equalsIgnoreCase(request.name())
                && brandRepository.existsByNameIgnoreCase(request.name())) {

            throw new DuplicateResourceException(
                    "Brand with name '" + request.name() + "' already exists");
        }

        brand.setName(request.name());
        brand.setDescription(request.description());
        brand.setWebsite(request.website());

        if (request.status() != null) {
            brand.setStatus(request.status());
        }

        if (request.logo() != null && !request.logo().isEmpty()) {

            String oldLogo = brand.getLogo();

            String newLogo = fileStorageService.store(
                    request.logo(),
                    "brands");

            brand.setLogo(newLogo);

            if (oldLogo != null) {
                fileStorageService.delete(oldLogo);
            }
        }

        Brand updatedBrand = brandRepository.save(brand);

        return toResponse(updatedBrand);
    }

    public void delete(Long id) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Brand with id " + id + " not found"));

        if (brand.getLogo() != null) {
            fileStorageService.delete(brand.getLogo());
        }

        brandRepository.delete(brand);
    }

    @Transactional(readOnly = true)
    public List<BrandResponse> findByStatus(boolean status) {

        return brandRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BrandResponse> searchByName(String name) {

        return brandRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private BrandResponse toResponse(Brand brand) {

        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getLogo(),
                brand.getDescription(),
                brand.getWebsite(),
                brand.isStatus());
    }
}