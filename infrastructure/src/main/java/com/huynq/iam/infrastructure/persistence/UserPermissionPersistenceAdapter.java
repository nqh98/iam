package com.huynq.iam.infrastructure.persistence;

import com.huynq.iam.core.domain.entity.UserPermissionEntity;
import com.huynq.iam.core.domain.repository.UserPermissionRepository;
import com.huynq.iam.infrastructure.persistence.jpa.entity.UserPermissionJpaEntity;
import com.huynq.iam.infrastructure.persistence.jpa.repository.UserPermissionJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserPermissionPersistenceAdapter implements UserPermissionRepository {

    private final UserPermissionJpaRepository userPermissionJpaRepository;

    public UserPermissionPersistenceAdapter(UserPermissionJpaRepository userPermissionJpaRepository) {
        this.userPermissionJpaRepository = userPermissionJpaRepository;
    }

    @Override
    public Set<UserPermissionEntity> findByUserId(Long userId) {
        Set<UserPermissionJpaEntity> entities = userPermissionJpaRepository.findByUserId(userId);
        if (entities == null || entities.isEmpty()) {
            return Set.of();
        }
        return entities.stream()
                .map(jpa -> UserPermissionEntity.fromRecord(jpa.toRecord()))
                .collect(Collectors.toUnmodifiableSet());
    }
}

