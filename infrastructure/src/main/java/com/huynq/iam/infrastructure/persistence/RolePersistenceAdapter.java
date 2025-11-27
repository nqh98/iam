package com.huynq.iam.infrastructure.persistence;

import com.huynq.iam.core.domain.entity.PermissionEntity;
import com.huynq.iam.core.domain.entity.RoleEntity;
import com.huynq.iam.core.domain.entity.RolePermissionEntity;
import com.huynq.iam.core.domain.entity.record.Role;
import com.huynq.iam.core.domain.repository.RoleRepository;
import com.huynq.iam.core.domain.repository.RolePermissionRepository;
import com.huynq.iam.infrastructure.persistence.jpa.entity.PermissionJpaEntity;
import com.huynq.iam.infrastructure.persistence.jpa.entity.RoleJpaEntity;
import com.huynq.iam.infrastructure.persistence.jpa.entity.RolePermissionJpaEntity;
import com.huynq.iam.infrastructure.persistence.jpa.entity.RolePermissionKey;
import com.huynq.iam.infrastructure.persistence.jpa.repository.PermissionJpaRepository;
import com.huynq.iam.infrastructure.persistence.jpa.repository.RoleJpaRepository;
import com.huynq.iam.infrastructure.persistence.jpa.repository.RolePermissionJpaRepository;
import com.huynq.iam.infrastructure.persistence.jpa.repository.UserRoleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RolePersistenceAdapter implements RoleRepository, RolePermissionRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;

    public RolePersistenceAdapter(RoleJpaRepository roleJpaRepository,
                                  PermissionJpaRepository permissionJpaRepository,
                                  RolePermissionJpaRepository rolePermissionJpaRepository,
                                  UserRoleJpaRepository userRoleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
        this.permissionJpaRepository = permissionJpaRepository;
        this.rolePermissionJpaRepository = rolePermissionJpaRepository;
        this.userRoleJpaRepository = userRoleJpaRepository;
    }

    @Override
    @Transactional
    public RoleEntity save(RoleEntity role) {
        RoleJpaEntity saved = roleJpaRepository.save(RoleJpaEntity.fromDomain(role));
        syncPermissions(saved.getId(), role.getPermissions());
        return mapToDomain(saved);
    }

    @Override
    public Optional<RoleEntity> findById(Long id) {
        return roleJpaRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public Optional<RoleEntity> findByName(String name) {
        return roleJpaRepository.findByNameIgnoreCase(name).map(this::mapToDomain);
    }

    @Override
    public boolean existsById(long roleId) {
        return roleJpaRepository.existsById(roleId);
    }

    @Override
    public boolean existsByName(String name) {
        return roleJpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    @Transactional
    public void deleteById(Long roleId) {
        userRoleJpaRepository.deleteByRoleId(roleId);
        rolePermissionJpaRepository.deleteByIdRoleId(roleId);
        roleJpaRepository.deleteById(roleId);
    }

    @Override
    public List<RoleEntity> findAll() {
        return roleJpaRepository.findAll().stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public Set<RoleEntity> findByIds(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Set.of();
        }
        return roleJpaRepository.findAllById(roleIds).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<RolePermissionEntity> findByRoleIds(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Set.of();
        }
        Set<RolePermissionJpaEntity> entities = rolePermissionJpaRepository.findByIdRoleIdIn(roleIds);
        if (entities == null || entities.isEmpty()) {
            return Set.of();
        }
        return entities.stream()
                .map(jpa -> RolePermissionEntity.fromRecord(jpa.toRecord()))
                .collect(Collectors.toUnmodifiableSet());
    }

    private RoleEntity mapToDomain(RoleJpaEntity roleJpaEntity) {
        Set<PermissionEntity> permissions = loadPermissionsForRole(roleJpaEntity.getId());
        Role record = roleJpaEntity.toRecord();
        record = Role.builder()
                .from(record)
                .setPermissions(permissions)
                .build();
        return RoleEntity.fromRecord(record);
    }

    private Set<PermissionEntity> loadPermissionsForRole(Long roleId) {
        Set<RolePermissionJpaEntity> links = rolePermissionJpaRepository.findByIdRoleId(roleId);
        if (links == null || links.isEmpty()) {
            return Set.of();
        }
        Set<Long> permissionIds = links.stream()
                .map(link -> link.getId().getPermissionId())
                .collect(Collectors.toSet());
        List<PermissionJpaEntity> permissions = permissionJpaRepository.findAllById(permissionIds);
        return permissions.stream()
                .map(jpa -> PermissionEntity.fromRecord(jpa.toRecord()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void syncPermissions(Long roleId, Set<PermissionEntity> permissions) {
        Set<Long> targetPermissionIds = permissions == null ? Set.of() : permissions.stream()
                .map(PermissionEntity::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<RolePermissionJpaEntity> current = rolePermissionJpaRepository.findByIdRoleId(roleId);
        Set<Long> currentIds = current.stream()
                .map(entry -> entry.getId().getPermissionId())
                .collect(Collectors.toSet());

        Set<Long> toAdd = new LinkedHashSet<>(targetPermissionIds);
        toAdd.removeAll(currentIds);

        Set<Long> toRemove = new LinkedHashSet<>(currentIds);
        toRemove.removeAll(targetPermissionIds);

        long now = System.currentTimeMillis();
        for (Long permissionId : toAdd) {
            rolePermissionJpaRepository.save(new RolePermissionJpaEntity(
                    new RolePermissionKey(roleId, permissionId),
                    now
            ));
        }
        if (!toRemove.isEmpty()) {
            toRemove.forEach(permissionId -> rolePermissionJpaRepository.deleteById(
                    new RolePermissionKey(roleId, permissionId)
            ));
        }
    }
}

