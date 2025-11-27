package com.huynq.iam.infrastructure.persistence;

import com.huynq.iam.core.domain.entity.PermissionEntity;
import com.huynq.iam.core.domain.repository.PermissionRepository;
import com.huynq.iam.infrastructure.persistence.jpa.entity.PermissionJpaEntity;
import com.huynq.iam.infrastructure.persistence.jpa.repository.PermissionJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class PermissionPersistenceAdapter implements PermissionRepository {

    private final PermissionJpaRepository permissionJpaRepository;

    public PermissionPersistenceAdapter(PermissionJpaRepository permissionJpaRepository) {
        this.permissionJpaRepository = permissionJpaRepository;
    }

    @Override
    public PermissionEntity save(PermissionEntity permission) {
        PermissionJpaEntity saved = permissionJpaRepository.save(PermissionJpaEntity.fromDomain(permission));
        return PermissionEntity.fromRecord(saved.toRecord());
    }

    @Override
    public Optional<PermissionEntity> findByName(String name) {
        return permissionJpaRepository.findByNameIgnoreCase(name)
                .map(jpa -> PermissionEntity.fromRecord(jpa.toRecord()));
    }

    @Override
    public Set<PermissionEntity> findByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Set.of();
        }
        List<PermissionJpaEntity> entities = permissionJpaRepository.findAllById(ids);
        return entities.stream()
                .map(jpa -> PermissionEntity.fromRecord(jpa.toRecord()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<PermissionEntity> findByNames(Set<String> names) {
        if (names == null || names.isEmpty()) {
            return Set.of();
        }
        Set<PermissionJpaEntity> entities = permissionJpaRepository.findByNameInIgnoreCase(names);
        if (entities == null || entities.isEmpty()) {
            return Set.of();
        }
        return entities.stream()
                .map(jpa -> PermissionEntity.fromRecord(jpa.toRecord()))
                .collect(Collectors.toUnmodifiableSet());
    }
}

