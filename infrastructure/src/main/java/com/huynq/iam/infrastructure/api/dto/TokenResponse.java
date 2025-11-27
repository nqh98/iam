package com.huynq.iam.infrastructure.api.dto;

import java.util.List;
import java.util.Set;

public record TokenResponse(
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

