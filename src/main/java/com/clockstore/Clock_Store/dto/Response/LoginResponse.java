package com.clockstore.Clock_Store.dto.Response;

public record LoginResponse(

        CustomerResponse customer,

        String accessToken,

        String refreshToken) {
}