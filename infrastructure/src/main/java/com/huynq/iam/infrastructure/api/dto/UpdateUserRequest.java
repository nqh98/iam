package com.huynq.iam.infrastructure.api.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank String externalId
) {
}

