package com.huynq.application.port.in.result;

import java.util.List;

public record RoleDetailsResult(
        Long id,
        String name,
        String description,
        boolean active,
        List<String> permissions,
        Long createdAt,
        Long updatedAt
) {
}

