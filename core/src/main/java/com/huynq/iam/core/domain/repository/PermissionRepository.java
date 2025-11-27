package com.huynq.iam.core.domain.repository;

import com.huynq.iam.core.domain.entity.PermissionEntity;

import java.util.Optional;
import java.util.Set;

public interface PermissionRepository {
    PermissionEntity save(PermissionEntity permission);

    Optional<PermissionEntity> findByName(String name);

    Set<PermissionEntity> findByIds(Set<Long> ids);

    Set<PermissionEntity> findByNames(Set<String> names);
}
