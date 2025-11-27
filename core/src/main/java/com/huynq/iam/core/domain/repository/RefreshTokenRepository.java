package com.huynq.iam.core.domain.repository;

import com.huynq.iam.core.domain.entity.RefreshTokenEntity;

import java.util.Optional;

/**
 * Abstraction over refresh token persistence so the domain/application
 * can enforce rotation and revocation policies without knowing about storage.
 */
public interface RefreshTokenRepository {

    RefreshTokenEntity save(RefreshTokenEntity refreshToken);

    Optional<RefreshTokenEntity> findByTokenId(String tokenId);

    void deleteByUserId(Long userId);

    void deleteBySessionId(String sessionId);

    void deleteExpiredBefore(Long timestampMillis);

    default Optional<RefreshTokenEntity> findActiveByTokenId(String tokenId, long nowMillis) {
        return findByTokenId(tokenId)
                .filter(token -> !token.isExpired(nowMillis))
                .filter(token -> !token.isRevoked());
    }
}

