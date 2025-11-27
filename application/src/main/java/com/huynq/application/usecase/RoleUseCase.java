package com.huynq.application.usecase;

import com.huynq.application.port.in.RoleCommandUseCase;
import com.huynq.application.port.in.RoleQueryUseCase;
import com.huynq.application.port.in.command.CreateRoleCommand;
import com.huynq.application.port.in.command.UpdateRoleCommand;
import com.huynq.application.port.in.result.RoleDetailsResult;
import com.huynq.iam.core.domain.entity.PermissionEntity;
import com.huynq.iam.core.domain.entity.RoleEntity;
import com.huynq.iam.core.domain.entity.record.Permission;
import com.huynq.iam.core.domain.entity.record.Role;
import com.huynq.iam.core.domain.enums.ErrorCode;
import com.huynq.iam.core.domain.exception.BusinessException;
import com.huynq.iam.core.domain.repository.PermissionRepository;
import com.huynq.iam.core.domain.repository.RoleRepository;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Use case responsible for managing roles and their permissions.
 */
public class RoleUseCase implements RoleCommandUseCase, RoleQueryUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleUseCase(RoleRepository roleRepository,
                       PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public RoleDetailsResult createRole(CreateRoleCommand command) throws BusinessException {
        if (roleRepository.existsByName(command.name())) {
            throw new BusinessException(
                    ErrorCode.ROLE_NAME_EXISTS.getCode(),
                    ErrorCode.ROLE_NAME_EXISTS.getDefaultMessage()
            );
        }
        long now = System.currentTimeMillis();
        Set<PermissionEntity> permissions = resolvePermissions(command.permissions(), now);
        Role roleRecord = Role.builder()
                .setName(command.name())
                .setDescription(command.description())
                .setActive(true)
                .setCreatedAt(now)
                .setUpdatedAt(now)
                .setPermissions(permissions)
                .build();
        RoleEntity saved = roleRepository.save(RoleEntity.fromRecord(roleRecord));
        return toResult(saved);
    }

    @Override
    public RoleDetailsResult updateRole(UpdateRoleCommand command) throws BusinessException {
        RoleEntity existing = roleRepository.findById(command.roleId()).orElseThrow(() -> new BusinessException(
                ErrorCode.ROLE_NOT_FOUND.getCode(),
                ErrorCode.ROLE_NOT_FOUND.getDefaultMessage()
        ));

        RoleEntity updated = existing;
        if (command.name() != null && !command.name().equalsIgnoreCase(existing.getName())) {
            Optional<RoleEntity> conflict = roleRepository.findByName(command.name());
            if (conflict.isPresent() && !conflict.get().getId().equals(existing.getId())) {
                throw new BusinessException(
                        ErrorCode.ROLE_NAME_EXISTS.getCode(),
                        ErrorCode.ROLE_NAME_EXISTS.getDefaultMessage()
                );
            }
            updated = updated.withName(command.name());
        }
        if (command.description() != null) {
            updated = updated.withDescription(command.description());
        }
        if (command.active() != null) {
            updated = command.active() ? updated.activate() : updated.deactivate();
        }
        if (command.permissions() != null) {
            Set<PermissionEntity> permissions = resolvePermissions(command.permissions(), System.currentTimeMillis());
            updated = updated.withPermissions(permissions);
        }

        RoleEntity saved = roleRepository.save(updated);
        return toResult(saved);
    }

    @Override
    public void deleteRole(Long roleId) throws BusinessException {
        if (!roleRepository.existsById(roleId)) {
            throw new BusinessException(
                    ErrorCode.ROLE_NOT_FOUND.getCode(),
                    ErrorCode.ROLE_NOT_FOUND.getDefaultMessage()
            );
        }
        roleRepository.deleteById(roleId);
    }

    @Override
    public RoleDetailsResult getRole(Long roleId) throws BusinessException {
        RoleEntity role = roleRepository.findById(roleId).orElseThrow(() -> new BusinessException(
                ErrorCode.ROLE_NOT_FOUND.getCode(),
                ErrorCode.ROLE_NOT_FOUND.getDefaultMessage()
        ));
        return toResult(role);
    }

    @Override
    public List<RoleDetailsResult> listRoles() {
        List<RoleEntity> roles = roleRepository.findAll();
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        return roles.stream()
                .map(this::toResult)
                .toList();
    }

    private RoleDetailsResult toResult(RoleEntity role) {
        List<String> permissions = role.getPermissions() == null
                ? List.of()
                : role.getPermissions().stream()
                .map(PermissionEntity::asScope)
                .filter(Objects::nonNull)
                .sorted()
                .toList();
        return new RoleDetailsResult(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.isActive(),
                permissions,
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }

    private Set<PermissionEntity> resolvePermissions(List<String> permissionScopes, long now) {
        if (permissionScopes == null || permissionScopes.isEmpty()) {
            return Set.of();
        }
        Set<String> normalized = permissionScopes.stream()
                .filter(scope -> scope != null && !scope.isBlank())
                .map(scope -> scope.trim().toLowerCase(Locale.ROOT))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (normalized.isEmpty()) {
            return Set.of();
        }

        Set<PermissionEntity> existing = permissionRepository.findByNames(normalized);
        Map<String, PermissionEntity> existingByName = existing == null ? Collections.emptyMap() :
                existing.stream()
                        .collect(Collectors.toMap(
                                permission -> permission.getName().toLowerCase(Locale.ROOT),
                                Function.identity(),
                                (left, right) -> left
                        ));

        Set<PermissionEntity> resolved = new LinkedHashSet<>();
        if (existing != null) {
            resolved.addAll(existing);
        }

        for (String scope : normalized) {
            if (!existingByName.containsKey(scope)) {
                PermissionEntity created = permissionRepository.save(buildPermission(scope, now));
                resolved.add(created);
            }
        }

        return resolved;
    }

    private PermissionEntity buildPermission(String scope, long now) {
        String[] parts = scope.split(":", 2);
        String resource = parts[0];
        String action = parts.length > 1 ? parts[1] : "access";
        Permission permissionRecord = Permission.builder()
                .setName(scope)
                .setResource(resource)
                .setAction(action)
                .setDescription(resource + ":" + action)
                .setActive(true)
                .setCreatedAt(now)
                .setUpdatedAt(now)
                .build();
        return PermissionEntity.fromRecord(permissionRecord);
    }
}

