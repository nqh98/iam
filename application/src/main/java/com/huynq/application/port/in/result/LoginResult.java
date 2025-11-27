package com.huynq.application.port.in.result;

import java.util.List;
import java.util.Set;

/**
 * Result returned by the Login use case.
 */
public record LoginResult(
        Long userId,
        String externalId,
        List<String> roles,
        Set<String> scopes,
        String tokenType,
        String accessToken,
        String refreshToken,
        Long accessTokenExpiresAt,
        Long refreshTokenExpiresAt,
        String sessionId
) {
}

