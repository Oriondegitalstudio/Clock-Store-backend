package com.clockstore.Clock_Store.dto.response;

public record LoginResponse(

        CustomerResponse customer,

        String accessToken,

        String refreshToken) {
}