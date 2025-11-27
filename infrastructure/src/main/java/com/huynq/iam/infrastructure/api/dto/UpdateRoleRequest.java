package com.huynq.iam.infrastructure.api.dto;

import java.util.List;

public record UpdateRoleRequest(
        String name,
        String description,
        Boolean active,
        List<String> permissions
) {
}

