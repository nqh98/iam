package com.huynq.iam.infrastructure.api.dto;

import java.util.List;

public record RoleResponse(
        Long id,
        String name,
        String description,
        boolean active,
        List<String> permissions,
        Long createdAt,
        Long updatedAt
) {
}

