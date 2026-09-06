package com.clockstore.Clock_Store.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clockstore.Clock_Store.dto.Request.ProductOptionValueRequest;
import com.clockstore.Clock_Store.dto.Response.ProductOptionValueResponse;
import com.clockstore.Clock_Store.entity.ProductOption;
import com.clockstore.Clock_Store.entity.ProductOptionValue;
import com.clockstore.Clock_Store.exception.ResourceNotFoundException;
import com.clockstore.Clock_Store.repository.ProductOptionRepository;
import com.clockstore.Clock_Store.repository.ProductOptionValueRepository;

@Service
@Transactional
public class ProductOptionValueService {

    private final ProductOptionValueRepository productOptionValueRepository;
    private final ProductOptionRepository productOptionRepository;
    private final FileStorageService fileStorageService;

    public ProductOptionValueService(
            ProductOptionValueRepository productOptionValueRepository,
            ProductOptionRepository productOptionRepository,
            FileStorageService fileStorageService) {

        this.productOptionValueRepository = productOptionValueRepository;
        this.productOptionRepository = productOptionRepository;
        this.fileStorageService = fileStorageService;
    }

    public ProductOptionValueResponse create(ProductOptionValueRequest request) {

        ProductOption option = productOptionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product option with id " + request.getOptionId() + " not found"));

        ProductOptionValue optionValue = new ProductOptionValue();

        optionValue.setOption(option);
        optionValue.setValue(request.getValue());
        optionValue.setColorCode(request.getColorCode());
        optionValue.setSortOrder(request.getSortOrder());

        String imagePath = fileStorageService.store(
                request.getImage(),
                "product-option-values");

        optionValue.setImage(imagePath);

        ProductOptionValue savedOptionValue = productOptionValueRepository.save(optionValue);

        return toResponse(savedOptionValue);
    }

    @Transactional(readOnly = true)
    public List<ProductOptionValueResponse> findAll() {

        return productOptionValueRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductOptionValueResponse findById(Long id) {

        ProductOptionValue optionValue = getOptionValue(id);

        return toResponse(optionValue);
    }

    @Transactional(readOnly = true)
    public List<ProductOptionValueResponse> findByOptionId(Long optionId) {

        if (!productOptionRepository.existsById(optionId)) {
            throw new ResourceNotFoundException(
                    "Product option with id " + optionId + " not found");
        }

        return productOptionValueRepository
                .findByOptionIdOrderBySortOrderAsc(optionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductOptionValueResponse update(
            Long id,
            ProductOptionValueRequest request) {

        ProductOptionValue optionValue = getOptionValue(id);

        ProductOption option = productOptionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product option with id " + request.getOptionId() + " not found"));

        optionValue.setOption(option);
        optionValue.setValue(request.getValue());
        optionValue.setColorCode(request.getColorCode());
        optionValue.setSortOrder(request.getSortOrder());

        /*
         * Replace the image only if a new file was uploaded.
         */
        if (request.getImage() != null && !request.getImage().isEmpty()) {

            String oldImage = optionValue.getImage();

            String newImage = fileStorageService.store(
                    request.getImage(),
                    "product-option-values");

            optionValue.setImage(newImage);

            /*
             * Delete the old image after the new one
             * has been successfully stored.
             */
            if (oldImage != null && !oldImage.isBlank()) {
                fileStorageService.delete(oldImage);
            }
        }

        ProductOptionValue updatedOptionValue = productOptionValueRepository.save(optionValue);

        return toResponse(updatedOptionValue);
    }

    public void delete(Long id) {

        ProductOptionValue optionValue = getOptionValue(id);

        String image = optionValue.getImage();

        productOptionValueRepository.delete(optionValue);

        /*
         * Delete the physical image as well.
         */
        if (image != null && !image.isBlank()) {
            fileStorageService.delete(image);
        }
    }

    private ProductOptionValue getOptionValue(Long id) {

        return productOptionValueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product option value with id " + id + " not found"));
    }

    private ProductOptionValueResponse toResponse(
            ProductOptionValue optionValue) {

        return new ProductOptionValueResponse(
                optionValue.getId(),
                optionValue.getOption().getId(),
                optionValue.getOption().getName(),
                optionValue.getValue(),
                optionValue.getColorCode(),
                optionValue.getImage(),
                optionValue.getSortOrder());
    }
}