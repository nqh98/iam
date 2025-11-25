package com.huynq.iam.core.domain.entity.record;

public record Permission(
        Long id,
        String name,
        String description,
        String resource,
        String action,
        boolean active,
        Long createdAt,
        Long updatedAt
) {
    public static class Builder {
        private Long id;
        private String name;
        private String description;
        private String resource;
        private String action;
        private boolean active;
        private Long createdAt;
        private Long updatedAt;

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

        public Builder setResource(String resource) {
            this.resource = resource;
            return this;
        }

        public Builder setAction(String action) {
            this.action = action;
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

        public Builder from(Permission permission) {
            this.id = permission.id();
            this.name = permission.name();
            this.description = permission.description();
            this.resource = permission.resource();
            this.action = permission.action();
            this.active = permission.active();
            this.createdAt = permission.createdAt();
            this.updatedAt = permission.updatedAt();
            return this;
        }

        public Permission build() {
            return new Permission(id, name, description, resource, action, active, createdAt, updatedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
