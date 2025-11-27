package com.huynq.iam.infrastructure.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String externalId,
        @NotBlank String password,
        String clientId,
        String userAgent
) {
}

