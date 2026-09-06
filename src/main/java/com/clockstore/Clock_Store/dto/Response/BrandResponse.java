package com.clockstore.Clock_Store.dto.Response;

public record BrandResponse(
                Long id,
                String name,
                String logo,
                String description,
                String website,
                boolean status) {
}
