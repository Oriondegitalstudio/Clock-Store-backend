package com.clockstore.Clock_Store.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clockstore.Clock_Store.dto.Request.ProductOptionRequest;
import com.clockstore.Clock_Store.dto.Response.ProductOptionResponse;
import com.clockstore.Clock_Store.entity.ProductOption;
import com.clockstore.Clock_Store.entity.Products;
import com.clockstore.Clock_Store.exception.ResourceNotFoundException;
import com.clockstore.Clock_Store.repository.ProductOptionRepository;
import com.clockstore.Clock_Store.repository.ProductRepository;

@Service
@Transactional
public class ProductOptionService {

    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;

    public ProductOptionService(
            ProductOptionRepository productOptionRepository,
            ProductRepository productRepository) {

        this.productOptionRepository = productOptionRepository;
        this.productRepository = productRepository;
    }

    public ProductOptionResponse create(ProductOptionRequest request) {

        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + request.getProductId() + " not found"));

        ProductOption option = new ProductOption();

        option.setProduct(product);
        option.setName(request.getName());
        option.setDisplayType(request.getDisplayType());
        option.setSortOrder(request.getSortOrder());

        ProductOption savedOption = productOptionRepository.save(option);

        return toResponse(savedOption);
    }

    @Transactional(readOnly = true)
    public List<ProductOptionResponse> findAll() {

        return productOptionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductOptionResponse findById(Long id) {

        ProductOption option = getOption(id);

        return toResponse(option);
    }

    @Transactional(readOnly = true)
    public List<ProductOptionResponse> findByProductId(Long productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(
                    "Product with id " + productId + " not found");
        }

        return productOptionRepository
                .findByProductIdOrderBySortOrderAsc(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductOptionResponse update(
            Long id,
            ProductOptionRequest request) {

        ProductOption option = getOption(id);

        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + request.getProductId() + " not found"));

        option.setProduct(product);
        option.setName(request.getName());
        option.setDisplayType(request.getDisplayType());
        option.setSortOrder(request.getSortOrder());

        ProductOption updatedOption = productOptionRepository.save(option);

        return toResponse(updatedOption);
    }

    public void delete(Long id) {

        ProductOption option = getOption(id);

        productOptionRepository.delete(option);
    }

    private ProductOption getOption(Long id) {

        return productOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product option with id " + id + " not found"));
    }

    private ProductOptionResponse toResponse(ProductOption option) {

        return new ProductOptionResponse(
                option.getId(),
                option.getProduct().getId(),
                option.getProduct().getName(),
                option.getName(),
                option.getDisplayType(),
                option.getSortOrder());
    }

}