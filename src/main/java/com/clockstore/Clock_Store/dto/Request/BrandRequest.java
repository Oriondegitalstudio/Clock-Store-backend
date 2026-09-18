package com.clockstore.Clock_Store.dto.Request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BrandRequest(
        @NotBlank(message = "Brand name is required") @Size(max = 255, message = "Brand name must not exceed 255 characters") String name,

        MultipartFile logo,

        String description,

        @Size(max = 500, message = "Website must not exceed 500 characters") String website,

        Boolean status) {
}
