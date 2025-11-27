package com.huynq.iam.infrastructure.api.dto;

import java.util.List;
import java.util.Set;

public record UserResponse(
        Long id,
        String externalId,
        Long createdAt,
        Long updatedAt,
        List<RoleResponse> roles,
        Set<String> scopes
) {
}

