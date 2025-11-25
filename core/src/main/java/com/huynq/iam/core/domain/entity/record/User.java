package com.huynq.iam.core.domain.entity.record;


import com.huynq.iam.core.domain.valueobject.Password;

import java.util.Set;

public record User(
        Long id,
        Password password,
        Long createdAt,
        Long updatedAt,
        Set<Long> roleIds,
        String externalId
) {
    public static class Builder {
        private Long id;
        private Password password;
        private Long createdAt;
        private Long updatedAt;
        private Set<Long> roleIds = Set.of();
        private String externalId;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setPassword(Password password) {
            this.password = password;
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

        public Builder setRoleIds(Set<Long> roleIds) {
            this.roleIds = roleIds == null ? Set.of() : roleIds;
            return this;
        }

        public Builder setExternalId(String externalId) {
            this.externalId = externalId;
            return this;
        }

        public Builder from(User user) {
            this.id = user.id();
            this.password = user.password();
            this.createdAt = user.createdAt();
            this.updatedAt = user.updatedAt();
            this.roleIds = user.roleIds();
            this.externalId = user.externalId();
            return this;
        }

        public User build() {
            Set<Long> ids = roleIds == null ? Set.of() : roleIds;
            return new User(id, password, createdAt, updatedAt, ids, externalId);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
