package com.huynq.iam.core.domain.repository;

import com.huynq.iam.core.domain.entity.RolePermissionEntity;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public interface RolePermissionRepository {
    Set<RolePermissionEntity> findByRoleIds(Set<Long> roleIds);

    default Set<Long> findPermissionIdsByRoleIds(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptySet();
        }
        Set<RolePermissionEntity> entries = findByRoleIds(roleIds);
        if (entries == null || entries.isEmpty()) {
            return Collections.emptySet();
        }
        return entries.stream()
                .map(RolePermissionEntity::getPermissionId)
                .collect(Collectors.toUnmodifiableSet());
    }
}

