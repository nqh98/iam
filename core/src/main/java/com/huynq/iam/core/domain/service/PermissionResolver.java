package com.huynq.iam.core.domain.service;

import com.huynq.iam.core.domain.entity.PermissionEntity;
import com.huynq.iam.core.domain.enums.UserPermissionOverrideType;
import com.huynq.iam.core.domain.repository.PermissionRepository;
import com.huynq.iam.core.domain.repository.RolePermissionRepository;
import com.huynq.iam.core.domain.repository.UserPermissionRepository;
import com.huynq.iam.core.domain.repository.UserRoleRepository;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Resolves the effective set of OAuth scopes for a user by combining
 * role-derived permissions with user-level overrides.
 */
public class PermissionResolver {

    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final PermissionRepository permissionRepository;

    public PermissionResolver(UserRoleRepository userRoleRepository,
                              RolePermissionRepository rolePermissionRepository,
                              UserPermissionRepository userPermissionRepository,
                              PermissionRepository permissionRepository) {
        this.userRoleRepository = Objects.requireNonNull(userRoleRepository, "userRoleRepository");
        this.rolePermissionRepository = Objects.requireNonNull(rolePermissionRepository, "rolePermissionRepository");
        this.userPermissionRepository = Objects.requireNonNull(userPermissionRepository, "userPermissionRepository");
        this.permissionRepository = Objects.requireNonNull(permissionRepository, "permissionRepository");
    }

    /**
     * Computes the final permission scopes for the provided user.
     *
     * @param userId internal user identifier
     * @return immutable set of scopes
     */
    public Set<String> resolvePermissionsForUser(Long userId) {
        if (userId == null) {
            return Collections.emptySet();
        }

        Set<Long> roleIds = userRoleRepository.findRoleIdsByUserId(userId);
        if (roleIds == null || roleIds.isEmpty()) {
            return resolveOverridesOnly(userId);
        }

        Set<Long> permissionIdsFromRoles = rolePermissionRepository.findPermissionIdsByRoleIds(roleIds);
        return mergeWithOverrides(userId, permissionIdsFromRoles);
    }

    private Set<String> resolveOverridesOnly(Long userId) {
        Map<UserPermissionOverrideType, Set<Long>> overrideMap = userPermissionRepository.groupIdsByOverrideType(userId);
        Set<Long> addOnly = overrideMap.getOrDefault(UserPermissionOverrideType.ADD, Collections.emptySet());
        if (addOnly.isEmpty()) {
            return Collections.emptySet();
        }
        return immutableScopes(toScopes(addOnly));
    }

    private Set<String> mergeWithOverrides(Long userId, Set<Long> basePermissionIds) {
        Map<UserPermissionOverrideType, Set<Long>> overrideMap = userPermissionRepository.groupIdsByOverrideType(userId);
        Set<Long> removeIds = overrideMap.getOrDefault(UserPermissionOverrideType.REMOVE, Collections.emptySet());
        Set<Long> addIds = overrideMap.getOrDefault(UserPermissionOverrideType.ADD, Collections.emptySet());

        Set<Long> effectiveIds = new LinkedHashSet<>();
        if (basePermissionIds != null) {
            effectiveIds.addAll(basePermissionIds);
        }
        effectiveIds.removeAll(removeIds);
        effectiveIds.addAll(addIds);

        if (effectiveIds.isEmpty()) {
            return Collections.emptySet();
        }

        return immutableScopes(toScopes(effectiveIds));
    }

    private Set<String> toScopes(Set<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return Collections.emptySet();
        }
        Set<PermissionEntity> permissions = permissionRepository.findByIds(permissionIds);
        if (permissions == null || permissions.isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> computed = permissions.stream()
                .map(PermissionEntity::asScope)
                .filter(scope -> scope != null && !scope.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (computed.isEmpty()) {
            return Collections.emptySet();
        }
        return computed;
    }

    private Set<String> immutableScopes(Set<String> scopes) {
        if (scopes == null || scopes.isEmpty()) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(scopes);
    }
}

