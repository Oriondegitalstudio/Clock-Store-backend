package com.clockstore.Clock_Store.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clockstore.Clock_Store.dto.Request.VariantAttributeRequest;
import com.clockstore.Clock_Store.dto.Response.VariantAttributeResponse;
import com.clockstore.Clock_Store.entity.ProductOption;
import com.clockstore.Clock_Store.entity.ProductOptionValue;
import com.clockstore.Clock_Store.entity.ProductVariant;
import com.clockstore.Clock_Store.entity.VariantAttribute;
import com.clockstore.Clock_Store.exception.ConflictException;
import com.clockstore.Clock_Store.exception.ResourceNotFoundException;
import com.clockstore.Clock_Store.repository.ProductOptionRepository;
import com.clockstore.Clock_Store.repository.ProductOptionValueRepository;
import com.clockstore.Clock_Store.repository.ProductVariantRepository;
import com.clockstore.Clock_Store.repository.VariantAttributeRepository;

@Service
@Transactional
public class VariantAttributeService {

    private final VariantAttributeRepository variantAttributeRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionValueRepository productOptionValueRepository;

    public VariantAttributeService(
            VariantAttributeRepository variantAttributeRepository,
            ProductVariantRepository productVariantRepository,
            ProductOptionRepository productOptionRepository,
            ProductOptionValueRepository productOptionValueRepository) {

        this.variantAttributeRepository = variantAttributeRepository;
        this.productVariantRepository = productVariantRepository;
        this.productOptionRepository = productOptionRepository;
        this.productOptionValueRepository = productOptionValueRepository;
    }

    public VariantAttributeResponse create(VariantAttributeRequest request) {

        ProductVariant variant = getVariant(request.getVariantId());

        ProductOption option = getOption(request.getOptionId());

        ProductOptionValue optionValue = getOptionValue(request.getOptionValueId());

        validateRelationships(variant, option, optionValue);

        if (variantAttributeRepository.existsByVariantIdAndOptionId(
                request.getVariantId(),
                request.getOptionId())) {

            throw new ConflictException(
                    "This option is already assigned to the variant");
        }

        VariantAttribute attribute = new VariantAttribute();

        attribute.setVariant(variant);
        attribute.setOption(option);
        attribute.setOptionValue(optionValue);

        return toResponse(
                variantAttributeRepository.save(attribute));
    }

    @Transactional(readOnly = true)
    public List<VariantAttributeResponse> findAll() {

        return variantAttributeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VariantAttributeResponse findById(Long id) {

        return toResponse(getAttribute(id));
    }

    @Transactional(readOnly = true)
    public List<VariantAttributeResponse> findByVariantId(Long variantId) {

        if (!productVariantRepository.existsById(variantId)) {
            throw new ResourceNotFoundException(
                    "Product variant with id " + variantId + " not found");
        }

        return variantAttributeRepository
                .findByVariantId(variantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public VariantAttributeResponse update(
            Long id,
            VariantAttributeRequest request) {

        VariantAttribute attribute = getAttribute(id);

        ProductVariant variant = getVariant(request.getVariantId());

        ProductOption option = getOption(request.getOptionId());

        ProductOptionValue optionValue = getOptionValue(request.getOptionValueId());

        validateRelationships(variant, option, optionValue);

        if (variantAttributeRepository
                .existsByVariantIdAndOptionIdAndIdNot(
                        request.getVariantId(),
                        request.getOptionId(),
                        id)) {

            throw new ConflictException(
                    "This option is already assigned to the variant");
        }

        attribute.setVariant(variant);
        attribute.setOption(option);
        attribute.setOptionValue(optionValue);

        return toResponse(
                variantAttributeRepository.save(attribute));
    }

    public void delete(Long id) {

        VariantAttribute attribute = getAttribute(id);

        variantAttributeRepository.delete(attribute);
    }

    private ProductVariant getVariant(Long id) {

        return productVariantRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product variant with id " + id + " not found"));
    }

    private ProductOption getOption(Long id) {

        return productOptionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product option with id " + id + " not found"));
    }

    private ProductOptionValue getOptionValue(Long id) {

        return productOptionValueRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product option value with id " + id + " not found"));
    }

    private void validateRelationships(
            ProductVariant variant,
            ProductOption option,
            ProductOptionValue optionValue) {

        if (!optionValue.getOption().getId().equals(option.getId())) {
            throw new ConflictException(
                    "Option value does not belong to the specified option");
        }

        if (!option.getProduct().getId()
                .equals(variant.getProduct().getId())) {

            throw new ConflictException(
                    "Option does not belong to the same product as the variant");
        }
    }

    private VariantAttribute getAttribute(Long id) {

        return variantAttributeRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variant attribute with id " + id + " not found"));
    }

    private VariantAttributeResponse toResponse(
            VariantAttribute attribute) {

        return new VariantAttributeResponse(
                attribute.getId(),
                attribute.getVariant().getId(),
                attribute.getVariant().getSku(),
                attribute.getOption().getId(),
                attribute.getOption().getName(),
                attribute.getOptionValue().getId(),
                attribute.getOptionValue().getValue());
    }
}