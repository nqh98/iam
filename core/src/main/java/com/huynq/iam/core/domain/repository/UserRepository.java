package com.huynq.iam.core.domain.repository;

import com.huynq.iam.core.domain.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {
    UserEntity save(UserEntity user);

    boolean existsByExternalId(String externalId);

    Optional<UserEntity> findById(Long id);
}
