package com.clockstore.Clock_Store.dto.Request;

import jakarta.validation.constraints.NotNull;

public class VariantAttributeRequest {

    @NotNull(message = "Variant ID is required")
    private Long variantId;

    @NotNull(message = "Option ID is required")
    private Long optionId;

    @NotNull(message = "Option value ID is required")
    private Long optionValueId;

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public Long getOptionValueId() {
        return optionValueId;
    }

    public void setOptionValueId(Long optionValueId) {
        this.optionValueId = optionValueId;
    }
}