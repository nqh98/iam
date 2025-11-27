package com.huynq.iam.core.domain.entity;

import com.huynq.iam.core.domain.entity.record.UserRole;

/**
 * Immutable representation of the user_roles join table.
 */
public class UserRoleEntity {
    private final Long userId;
    private final Long roleId;
    private final Long assignedAt;

    UserRoleEntity(UserRole record) {
        this.userId = record.userId();
        this.roleId = record.roleId();
        this.assignedAt = record.assignedAt();
    }

    public Long getUserId() {
        return userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public Long getAssignedAt() {
        return assignedAt;
    }

    public UserRole toRecord() {
        return new UserRole(userId, roleId, assignedAt);
    }

    public static UserRoleEntity fromRecord(UserRole record) {
        return new UserRoleEntity(record);
    }
}

