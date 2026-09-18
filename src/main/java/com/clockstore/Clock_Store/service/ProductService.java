package com.clockstore.Clock_Store.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.clockstore.Clock_Store.dto.Request.ProductRequest;
import com.clockstore.Clock_Store.dto.Response.ProductResponse;
import com.clockstore.Clock_Store.entity.Brand;
import com.clockstore.Clock_Store.entity.Category;
import com.clockstore.Clock_Store.entity.ProductImage;
import com.clockstore.Clock_Store.entity.Products;
import com.clockstore.Clock_Store.entity.enums.ProductStatus;
import com.clockstore.Clock_Store.entity.enums.ProductVisibility;
import com.clockstore.Clock_Store.exception.DuplicateResourceException;
import com.clockstore.Clock_Store.exception.ResourceNotFoundException;
import com.clockstore.Clock_Store.repository.BrandRepository;
import com.clockstore.Clock_Store.repository.CategoryRepository;
import com.clockstore.Clock_Store.repository.ProductImageRepository;
import com.clockstore.Clock_Store.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

        private final ProductRepository productRepository;
        private final BrandRepository brandRepository;
        private final CategoryRepository categoryRepository;
        private final ProductImageRepository productImageRepository;
        private final FileStorageService fileStorageService;

        public ProductService(
                        ProductRepository productRepository,
                        BrandRepository brandRepository,
                        CategoryRepository categoryRepository,
                        ProductImageRepository productImageRepository,
                        FileStorageService fileStorageService) {

                this.productRepository = productRepository;
                this.brandRepository = brandRepository;
                this.categoryRepository = categoryRepository;
                this.productImageRepository = productImageRepository;
                this.fileStorageService = fileStorageService;
        }

        public ProductResponse create(ProductRequest request) {

                validateUniqueFields(request);

                Brand brand = brandRepository.findById(request.brandId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Brand with id " + request.brandId() + " not found"));

                Category category = categoryRepository.findById(request.categoryId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Category with id " + request.categoryId() + " not found"));

                Products product = new Products();

                setProductFields(product, request, brand, category);

                Products savedProduct = productRepository.save(product);

                saveImages(savedProduct, request.images());

                return toResponse(savedProduct);
        }

        @Transactional(readOnly = true)
        public List<ProductResponse> findAll() {

                return productRepository.findAll()
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public ProductResponse findById(Long id) {

                Products product = getProduct(id);

                return toResponse(product);
        }

        public ProductResponse update(Long id, ProductRequest request) {

                Products product = getProduct(id);

                validateUniqueFieldsForUpdate(id, request);

                Brand brand = brandRepository.findById(request.brandId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Brand with id " + request.brandId() + " not found"));

                Category category = categoryRepository.findById(request.categoryId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Category with id " + request.categoryId() + " not found"));

                setProductFields(product, request, brand, category);

                /*
                 * Only replace images when new images are actually provided.
                 */
                if (request.images() != null && !request.images().isEmpty()) {
                        replaceImages(product, request.images());
                }

                Products updatedProduct = productRepository.save(product);

                return toResponse(updatedProduct);
        }

        public void delete(Long id) {

                Products product = getProduct(id);

                List<ProductImage> images = productImageRepository.findByProductIdOrderBySortOrderAsc(id);

                for (ProductImage image : images) {
                        fileStorageService.delete(image.getUrl());
                }

                productImageRepository.deleteByProductId(id);

                productRepository.delete(product);
        }

        @Transactional(readOnly = true)
        public List<ProductResponse> findByStatus(ProductStatus status) {

                return productRepository.findByStatus(status)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public List<ProductResponse> findByVisibility(
                        ProductVisibility visibility) {

                return productRepository.findByVisibility(visibility)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public List<ProductResponse> findFeatured() {

                return productRepository.findByFeaturedTrue()
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public List<ProductResponse> searchByName(String name) {

                return productRepository.findByNameContainingIgnoreCase(name)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        private Products getProduct(Long id) {

                return productRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Product with id " + id + " not found"));
        }

        private void validateUniqueFields(ProductRequest request) {

                if (productRepository.existsBySlugIgnoreCase(request.slug())) {
                        throw new DuplicateResourceException(
                                        "Product with slug '" + request.slug() + "' already exists");
                }

                if (productRepository.existsBySkuIgnoreCase(request.sku())) {
                        throw new DuplicateResourceException(
                                        "Product with SKU '" + request.sku() + "' already exists");
                }
        }

        private void validateUniqueFieldsForUpdate(
                        Long id,
                        ProductRequest request) {

                Products existing = getProduct(id);

                if (!existing.getSlug().equalsIgnoreCase(request.slug())
                                && productRepository.existsBySlugIgnoreCase(request.slug())) {

                        throw new DuplicateResourceException(
                                        "Product with slug '" + request.slug() + "' already exists");
                }

                if (!existing.getSku().equalsIgnoreCase(request.sku())
                                && productRepository.existsBySkuIgnoreCase(request.sku())) {

                        throw new DuplicateResourceException(
                                        "Product with SKU '" + request.sku() + "' already exists");
                }
        }

        private void setProductFields(
                        Products product,
                        ProductRequest request,
                        Brand brand,
                        Category category) {

                product.setName(request.name());
                product.setSlug(request.slug());
                product.setSku(request.sku());
                product.setBarcode(request.barcode());
                product.setBrand(brand);
                product.setCategory(category);
                product.setShortDescription(request.shortDescription());
                product.setDescription(request.description());
                product.setWeight(request.weight());
                product.setWarranty(request.warranty());
                product.setStatus(request.status());
                product.setVisibility(request.visibility());
                product.setFeatured(request.featured());
        }

        private void saveImages(
                        Products product,
                        List<MultipartFile> files) {

                if (files == null || files.isEmpty()) {
                        return;
                }

                int sortOrder = 0;

                for (MultipartFile file : files) {

                        if (file == null || file.isEmpty()) {
                                continue;
                        }

                        String imageUrl = fileStorageService.store(
                                        file,
                                        "products");

                        ProductImage image = new ProductImage();

                        image.setProduct(product);
                        image.setUrl(imageUrl);
                        image.setSortOrder(sortOrder);
                        image.setPrimary(sortOrder == 0);

                        productImageRepository.save(image);

                        sortOrder++;
                }
        }

        private void replaceImages(
                        Products product,
                        List<MultipartFile> files) {

                List<ProductImage> existingImages = productImageRepository
                                .findByProductIdOrderBySortOrderAsc(product.getId());

                for (ProductImage image : existingImages) {
                        fileStorageService.delete(image.getUrl());
                }

                productImageRepository.deleteByProductId(product.getId());

                saveImages(product, files);
        }

        private ProductResponse toResponse(Products product) {

                List<String> images = productImageRepository
                                .findByProductIdOrderBySortOrderAsc(product.getId())
                                .stream()
                                .map(ProductImage::getUrl)
                                .toList();

                return new ProductResponse(
                                product.getId(),
                                product.getName(),
                                product.getSlug(),
                                product.getSku(),
                                product.getBarcode(),
                                product.getBrand().getId(),
                                product.getBrand().getName(),
                                product.getCategory().getId(),
                                product.getCategory().getCategoryName(),
                                product.getShortDescription(),
                                product.getDescription(),
                                product.getWeight(),
                                product.getWarranty(),
                                product.getStatus(),
                                product.getVisibility(),
                                product.isFeatured(),
                                product.getCreatedAt(),
                                product.getUpdatedAt(),
                                images);
        }
}