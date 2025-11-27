package com.huynq.iam.core.domain.repository;

import com.huynq.iam.core.domain.entity.UserPermissionEntity;
import com.huynq.iam.core.domain.enums.UserPermissionOverrideType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface UserPermissionRepository {
    Set<UserPermissionEntity> findByUserId(Long userId);

    default Map<UserPermissionOverrideType, Set<Long>> groupIdsByOverrideType(Long userId) {
        if (userId == null) {
            return Collections.emptyMap();
        }
        Set<UserPermissionEntity> entries = findByUserId(userId);
        if (entries == null || entries.isEmpty()) {
            return Collections.emptyMap();
        }
        return entries.stream()
                .collect(Collectors.groupingBy(
                        UserPermissionEntity::getOverrideType,
                        () -> new EnumMap<>(UserPermissionOverrideType.class),
                        Collectors.mapping(UserPermissionEntity::getPermissionId, Collectors.toUnmodifiableSet())
                ));
    }
}

