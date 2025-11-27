package com.huynq.iam.infrastructure.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateRoleRequest(
        @NotBlank String name,
        String description,
        List<String> permissions
) {
}

