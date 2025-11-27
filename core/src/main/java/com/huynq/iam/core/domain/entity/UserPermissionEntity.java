package com.huynq.iam.core.domain.entity;

import com.huynq.iam.core.domain.entity.record.UserPermission;
import com.huynq.iam.core.domain.enums.UserPermissionOverrideType;

/**
 * Immutable representation of user-level permission overrides.
 */
public class UserPermissionEntity {
    private final Long id;
    private final Long userId;
    private final Long permissionId;
    private final UserPermissionOverrideType overrideType;
    private final Long createdAt;
    private final Long updatedAt;

    UserPermissionEntity(UserPermission record) {
        this.id = record.id();
        this.userId = record.userId();
        this.permissionId = record.permissionId();
        this.overrideType = record.overrideType();
        this.createdAt = record.createdAt();
        this.updatedAt = record.updatedAt();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public UserPermissionOverrideType getOverrideType() {
        return overrideType;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public boolean isAddOverride() {
        return overrideType != null && overrideType.isAdd();
    }

    public boolean isRemoveOverride() {
        return overrideType != null && overrideType.isRemove();
    }

    public UserPermission toRecord() {
        return new UserPermission(id, userId, permissionId, overrideType, createdAt, updatedAt);
    }

    public static UserPermissionEntity fromRecord(UserPermission record) {
        return new UserPermissionEntity(record);
    }
}

