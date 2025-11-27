package com.huynq.iam.infrastructure.security;

import java.util.Set;

public record AuthenticatedUser(
        Long userId,
        String externalId,
        String sessionId,
        Set<String> scopes
) {
}

