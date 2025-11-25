package com.huynq.iam.core.domain.entity.record;


import com.huynq.iam.core.domain.entity.PermissionEntity;

import java.util.Set;

public record Role(
        Long id,
        String name,
        String description,
        boolean active,
        Long createdAt,
        Long updatedAt,
        Set<PermissionEntity> permissionEntities
) {
    public static class Builder {
        private Long id;
        private String name;
        private String description;
        private boolean active;
        private Long createdAt;
        private Long updatedAt;
        private Set<PermissionEntity> permissionEntities;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setActive(boolean active) {
            this.active = active;
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

        public Builder setPermissions(Set<PermissionEntity> permissionEntities) {
            this.permissionEntities = permissionEntities;
            return this;
        }

        public Builder from(Role role) {
            this.id = role.id();
            this.name = role.name();
            this.description = role.description();
            this.active = role.active();
            this.createdAt = role.createdAt();
            this.updatedAt = role.updatedAt();
            this.permissionEntities = role.permissionEntities();
            return this;
        }

        public Role build() {
            return new Role(id, name, description, active, createdAt, updatedAt, permissionEntities);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
