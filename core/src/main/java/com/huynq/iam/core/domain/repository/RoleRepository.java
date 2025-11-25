package com.huynq.iam.core.domain.repository;

import com.huynq.iam.core.domain.entity.RoleEntity;

import java.util.Optional;

public interface RoleRepository {
    Optional<RoleEntity> findById(Long id);

    boolean existsById(long roleId);
}
