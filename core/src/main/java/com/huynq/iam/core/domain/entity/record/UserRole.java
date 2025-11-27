package com.huynq.iam.core.domain.entity.record;

public record UserRole(
        Long userId,
        Long roleId,
        Long assignedAt
) {
    public static class Builder {
        private Long userId;
        private Long roleId;
        private Long assignedAt;

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setRoleId(Long roleId) {
            this.roleId = roleId;
            return this;
        }

        public Builder setAssignedAt(Long assignedAt) {
            this.assignedAt = assignedAt;
            return this;
        }

        public Builder from(UserRole record) {
            this.userId = record.userId();
            this.roleId = record.roleId();
            this.assignedAt = record.assignedAt();
            return this;
        }

        public UserRole build() {
            return new UserRole(userId, roleId, assignedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

