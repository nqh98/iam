package com.huynq.iam.core.domain.valueobject;

public record TokenPair(
        String sessionId,
        String accessToken,
        String refreshToken,
        long accessTokenExpiresAt,
        long refreshTokenExpiresAt,
        String refreshTokenId
) {
}

