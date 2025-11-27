package com.huynq.iam.infrastructure.persistence.jpa.repository;

import com.huynq.iam.infrastructure.persistence.jpa.entity.RefreshTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenJpaEntity, Long> {

    Optional<RefreshTokenJpaEntity> findByTokenId(String tokenId);

    void deleteByUserId(Long userId);

    void deleteBySessionId(String sessionId);

    void deleteByExpiresAtLessThanEqual(Long timestamp);
}

