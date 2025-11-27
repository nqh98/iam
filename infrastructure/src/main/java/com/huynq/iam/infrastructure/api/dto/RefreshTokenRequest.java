package com.huynq.iam.infrastructure.api.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank String refreshToken,
        String clientId,
        String userAgent,
        String ipAddress
) {
}

