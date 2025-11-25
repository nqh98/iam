package com.huynq.iam.core.domain.service;

import com.huynq.iam.core.domain.entity.PermissionEntity;
import com.huynq.iam.core.domain.entity.RoleEntity;
import com.huynq.iam.core.domain.entity.UserEntity;

import java.util.Set;

public interface AccessControlService {
    /**
     * Checks if a user has a specific permission.
     *
     * @param userId         the internal ID of the user
     * @param permissionName the name of the permission to check
     * @return true if the user has the permission
     */
    boolean hasPermission(Long userId, String permissionName);

    /**
     * Checks if a user has access to a specific resource and action.
     *
     * @param userId   the internal ID of the user
     * @param resource the resource name
     * @param action   the action name
     * @return true if the user has access
     */
    boolean hasAccess(Long userId, String resource, String action);

    /**
     * Gets all permissions for a user.
     *
     * @param userId the internal ID of the user
     * @return set of permissions the user has
     */
    Set<PermissionEntity> getUserPermissions(Long userId);

    /**
     * Gets all roles for a user.
     *
     * @param userId the internal ID of the user
     * @return set of roles the user has
     */
    Set<RoleEntity> getUserRoles(Long userId);

    /**
     * Checks if a user has a specific role.
     *
     * @param userId   the internal ID of the user
     * @param roleName the name of the role to check
     * @return true if the user has the role
     */
    boolean hasRole(Long userId, String roleName);

    /**
     * Grants a permission directly to a user.
     *
     * @param userId       the internal ID of the user
     * @param permissionId the internal ID of the permission to grant
     * @return the updated user
     */
    UserEntity grantPermission(Long userId, Long permissionId);

    /**
     * Revokes a permission from a user.
     *
     * @param userId       the internal ID of the user
     * @param permissionId the internal ID of the permission to revoke
     * @return the updated user
     */
    UserEntity revokePermission(Long userId, Long permissionId);

    /**
     * Checks if a role has a specific permission.
     *
     * @param roleId         the internal ID of the role
     * @param permissionName the name of the permission to check
     * @return true if the role has the permission
     */
    boolean roleHasPermission(Long roleId, String permissionName);
}
