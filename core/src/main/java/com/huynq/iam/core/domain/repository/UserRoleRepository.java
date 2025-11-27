package com.huynq.iam.core.domain.repository;

import com.huynq.iam.core.domain.entity.UserRoleEntity;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public interface UserRoleRepository {
    Set<UserRoleEntity> findByUserId(Long userId);

    default Set<Long> findRoleIdsByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptySet();
        }
        Set<UserRoleEntity> entries = findByUserId(userId);
        if (entries == null || entries.isEmpty()) {
            return Collections.emptySet();
        }
        return entries.stream()
                .map(UserRoleEntity::getRoleId)
                .collect(Collectors.toUnmodifiableSet());
    }
}

