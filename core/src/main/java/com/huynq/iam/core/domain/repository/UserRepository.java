package com.huynq.iam.core.domain.repository;

import com.huynq.iam.core.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    UserEntity save(UserEntity user);

    boolean existsByExternalId(String externalId);

    Optional<UserEntity> findByExternalId(String externalId);

    Optional<UserEntity> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<UserEntity> findAll();
}
