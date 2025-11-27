package com.huynq.iam.infrastructure.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String externalId,
        @NotBlank String password
) {
}

