package com.clockstore.Clock_Store.dto.Request;

import jakarta.validation.constraints.NotBlank;

public record EmailVerificationRequest(

        @NotBlank(message = "Verification token is required") String token

) {
}