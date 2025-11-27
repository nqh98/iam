package com.huynq.iam.infrastructure.persistence;

import com.huynq.iam.core.domain.entity.RefreshTokenEntity;
import com.huynq.iam.core.domain.repository.RefreshTokenRepository;
import com.huynq.iam.infrastructure.persistence.jpa.entity.RefreshTokenJpaEntity;
import com.huynq.iam.infrastructure.persistence.jpa.repository.RefreshTokenJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RefreshTokenPersistenceAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    public RefreshTokenPersistenceAdapter(RefreshTokenJpaRepository refreshTokenJpaRepository) {
        this.refreshTokenJpaRepository = refreshTokenJpaRepository;
    }

    @Override
    public RefreshTokenEntity save(RefreshTokenEntity refreshToken) {
        RefreshTokenJpaEntity saved = refreshTokenJpaRepository.save(RefreshTokenJpaEntity.fromDomain(refreshToken));
        return RefreshTokenEntity.fromRecord(saved.toRecord());
    }

    @Override
    public Optional<RefreshTokenEntity> findByTokenId(String tokenId) {
        return refreshTokenJpaRepository.findByTokenId(tokenId)
                .map(entity -> RefreshTokenEntity.fromRecord(entity.toRecord()));
    }

    @Override
    public void deleteByUserId(Long userId) {
        refreshTokenJpaRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteBySessionId(String sessionId) {
        refreshTokenJpaRepository.deleteBySessionId(sessionId);
    }

    @Override
    public void deleteExpiredBefore(Long timestampMillis) {
        refreshTokenJpaRepository.deleteByExpiresAtLessThanEqual(timestampMillis);
    }
}

