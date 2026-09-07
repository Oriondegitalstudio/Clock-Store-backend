package com.clockstore.Clock_Store.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clockstore.Clock_Store.dto.Request.ProductVariantRequest;
import com.clockstore.Clock_Store.dto.Response.ProductVariantResponse;
import com.clockstore.Clock_Store.entity.ProductVariant;
import com.clockstore.Clock_Store.entity.Products;
import com.clockstore.Clock_Store.exception.DuplicateResourceException;
import com.clockstore.Clock_Store.exception.ResourceNotFoundException;
import com.clockstore.Clock_Store.repository.ProductRepository;
import com.clockstore.Clock_Store.repository.ProductVariantRepository;

@Service
@Transactional
public class ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    public ProductVariantService(
            ProductVariantRepository productVariantRepository,
            ProductRepository productRepository) {

        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
    }

    public ProductVariantResponse create(ProductVariantRequest request) {

        Products product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + request.getProductId() + " not found"));

        if (productVariantRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException(
                    "Product variant with SKU " + request.getSku() + " already exists");
        }

        ProductVariant variant = new ProductVariant();

        variant.setProduct(product);
        variant.setSku(request.getSku());
        variant.setBarcode(request.getBarcode());
        variant.setWeight(request.getWeight());
        variant.setStatus(request.getStatus());

        ProductVariant savedVariant = productVariantRepository.save(variant);

        return toResponse(savedVariant);
    }

    @Transactional(readOnly = true)
    public List<ProductVariantResponse> findAll() {

        return productVariantRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductVariantResponse findById(Long id) {

        return toResponse(getVariant(id));
    }

    @Transactional(readOnly = true)
    public List<ProductVariantResponse> findByProductId(Long productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(
                    "Product with id " + productId + " not found");
        }

        return productVariantRepository
                .findByProductId(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductVariantResponse update(
            Long id,
            ProductVariantRequest request) {

        ProductVariant variant = getVariant(id);

        Products product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + request.getProductId() + " not found"));

        if (productVariantRepository
                .existsBySkuAndIdNot(request.getSku(), id)) {

            throw new DuplicateResourceException(
                    "Product variant with SKU " + request.getSku() + " already exists");
        }

        variant.setProduct(product);
        variant.setSku(request.getSku());
        variant.setBarcode(request.getBarcode());
        variant.setWeight(request.getWeight());
        variant.setStatus(request.getStatus());

        ProductVariant updatedVariant = productVariantRepository.save(variant);

        return toResponse(updatedVariant);
    }

    public void delete(Long id) {

        ProductVariant variant = getVariant(id);

        productVariantRepository.delete(variant);
    }

    private ProductVariant getVariant(Long id) {

        return productVariantRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product variant with id " + id + " not found"));
    }

    private ProductVariantResponse toResponse(ProductVariant variant) {

        return new ProductVariantResponse(
                variant.getId(),
                variant.getProduct().getId(),
                variant.getProduct().getName(),
                variant.getSku(),
                variant.getBarcode(),
                variant.getWeight(),
                variant.getStatus());
    }
}