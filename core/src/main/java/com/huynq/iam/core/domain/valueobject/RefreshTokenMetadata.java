package com.huynq.iam.core.domain.valueobject;

import java.util.List;
import java.util.Set;

/**
 * Snapshot of claims extracted from a refresh token JWT.
 */
public record RefreshTokenMetadata(
        String tokenId,
        String sessionId,
        Long userId,
        String externalId,
        List<String> roles,
        Set<String> scopes,
        long expiresAt
) {
}

