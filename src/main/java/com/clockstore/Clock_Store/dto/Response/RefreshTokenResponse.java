package com.clockstore.Clock_Store.dto.Response;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken) {
}
