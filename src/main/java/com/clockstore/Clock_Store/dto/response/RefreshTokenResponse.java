package com.clockstore.Clock_Store.dto.response;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken) {
}
