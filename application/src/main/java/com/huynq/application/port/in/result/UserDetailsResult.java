package com.huynq.application.port.in.result;

import java.util.List;
import java.util.Set;

public record UserDetailsResult(
        Long id,
        String externalId,
        Long createdAt,
        Long updatedAt,
        List<RoleDetailsResult> roles,
        Set<String> scopes
) {
}

