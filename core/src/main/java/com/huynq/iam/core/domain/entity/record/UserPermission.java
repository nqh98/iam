package com.huynq.iam.core.domain.entity.record;

import com.huynq.iam.core.domain.enums.UserPermissionOverrideType;

public record UserPermission(
        Long id,
        Long userId,
        Long permissionId,
        UserPermissionOverrideType overrideType,
        Long createdAt,
        Long updatedAt
) {
    public static class Builder {
        private Long id;
        private Long userId;
        private Long permissionId;
        private UserPermissionOverrideType overrideType;
        private Long createdAt;
        private Long updatedAt;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setPermissionId(Long permissionId) {
            this.permissionId = permissionId;
            return this;
        }

        public Builder setOverrideType(UserPermissionOverrideType overrideType) {
            this.overrideType = overrideType;
            return this;
        }

        public Builder setCreatedAt(Long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setUpdatedAt(Long updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder from(UserPermission record) {
            this.id = record.id();
            this.userId = record.userId();
            this.permissionId = record.permissionId();
            this.overrideType = record.overrideType();
            this.createdAt = record.createdAt();
            this.updatedAt = record.updatedAt();
            return this;
        }

        public UserPermission build() {
            return new UserPermission(id, userId, permissionId, overrideType, createdAt, updatedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

