package com.huynq.iam.core.domain.entity.record;

public record RolePermission(
        Long roleId,
        Long permissionId,
        Long createdAt
) {
    public static class Builder {
        private Long roleId;
        private Long permissionId;
        private Long createdAt;

        public Builder setRoleId(Long roleId) {
            this.roleId = roleId;
            return this;
        }

        public Builder setPermissionId(Long permissionId) {
            this.permissionId = permissionId;
            return this;
        }

        public Builder setCreatedAt(Long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder from(RolePermission record) {
            this.roleId = record.roleId();
            this.permissionId = record.permissionId();
            this.createdAt = record.createdAt();
            return this;
        }

        public RolePermission build() {
            return new RolePermission(roleId, permissionId, createdAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

