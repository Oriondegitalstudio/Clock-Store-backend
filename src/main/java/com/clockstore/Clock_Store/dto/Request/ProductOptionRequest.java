package com.clockstore.Clock_Store.dto.Request;

import com.clockstore.Clock_Store.entity.enums.OptionDisplayType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductOptionRequest {
        @NotNull(message = "Product ID is required")
        Long productId;

        @NotBlank(message = "Option name is required")
        @Size(max = 255, message = "Option name must not exceed 255 characters")
        String name;

        @NotNull(message = "Display type is required")
        OptionDisplayType displayType;

        Integer sortOrder;


        // getters and setters
        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public OptionDisplayType getDisplayType() {
            return displayType;
        }

        public void setDisplayType(OptionDisplayType displayType) {
            this.displayType = displayType;
        }

        public Integer getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
        }
    }
