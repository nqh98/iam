package com.huynq.iam.core.domain.enums;

/**
 * Indicates whether a user-specific permission entry adds or removes a scope
 * when compared to the default permission set derived from the user's roles.
 */
public enum UserPermissionOverrideType {
    ADD,
    REMOVE;

    public boolean isAdd() {
        return this == ADD;
    }

    public boolean isRemove() {
        return this == REMOVE;
    }
}

