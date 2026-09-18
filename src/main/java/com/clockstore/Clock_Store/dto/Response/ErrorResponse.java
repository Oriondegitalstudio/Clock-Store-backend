package com.clockstore.Clock_Store.dto.Response;

public record ErrorResponse(
        int status,
        String message) {
}