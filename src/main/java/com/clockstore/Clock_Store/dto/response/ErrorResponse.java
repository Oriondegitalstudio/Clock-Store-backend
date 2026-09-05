package com.clockstore.Clock_Store.dto.response;

public record ErrorResponse(
        int status,
        String message) {
}