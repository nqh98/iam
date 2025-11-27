package com.huynq.iam.core.domain.entity;

import com.huynq.iam.core.domain.entity.record.RolePermission;

/**
 * Immutable representation of the role_permissions join table.
 */
public class RolePermissionEntity {
    private final Long roleId;
    private final Long permissionId;
    private final Long createdAt;

    RolePermissionEntity(RolePermission record) {
        this.roleId = record.roleId();
        this.permissionId = record.permissionId();
        this.createdAt = record.createdAt();
    }

    public Long getRoleId() {
        return roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public RolePermission toRecord() {
        return new RolePermission(roleId, permissionId, createdAt);
    }

    public static RolePermissionEntity fromRecord(RolePermission record) {
        return new RolePermissionEntity(record);
    }
}

