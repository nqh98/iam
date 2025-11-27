package com.huynq.iam.core.domain.entity;


import com.huynq.iam.core.domain.entity.record.Permission;

/**
 * Permission entity representing a permission in the identity and access management system.
 * This class is immutable and thread-safe.
 * Use {@link Permission} to create instances.
 */
public class PermissionEntity {
    private final Long id;
    private final String name;
    private final String description;
    private final String resource;
    private final String action;
    private final boolean active;
    private final Long createdAt;
    private final Long updatedAt;

    // Package-private constructor - use builder
    PermissionEntity(Permission permission) {
        this.id = permission.id();
        this.name = permission.name();
        this.description = permission.description();
        this.resource = permission.resource();
        this.action = permission.action();
        this.active = permission.active();
        this.createdAt = permission.createdAt();
        this.updatedAt = permission.updatedAt();
    }

    // Getters only (immutable)
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getResource() {
        return resource;
    }

    public String getAction() {
        return action;
    }

    public boolean isActive() {
        return active;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    // Business methods - return new instances (immutable)
    public PermissionEntity activate() {
        Permission updatedPermission = Permission.builder()
                .from(this.toRecord())
                .setActive(true)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new PermissionEntity(updatedPermission);
    }

    public PermissionEntity deactivate() {
        Permission updatedPermission = Permission.builder()
                .from(this.toRecord())
                .setActive(false)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new PermissionEntity(updatedPermission);
    }

    public PermissionEntity withResource(String newResource) {
        Permission updatedPermission = Permission.builder()
                .from(this.toRecord())
                .setResource(newResource)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new PermissionEntity(updatedPermission);
    }

    public PermissionEntity withAction(String newAction) {
        Permission updatedPermission = Permission.builder()
                .from(this.toRecord())
                .setAction(newAction)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new PermissionEntity(updatedPermission);
    }

    public PermissionEntity withDescription(String newDescription) {
        Permission updatedPermission = Permission.builder()
                .from(this.toRecord())
                .setDescription(newDescription)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new PermissionEntity(updatedPermission);
    }

    public boolean isForResource(String resourceName) {
        return this.resource.equals(resourceName);
    }

    public boolean isForAction(String actionName) {
        return this.action.equals(actionName);
    }

    /**
     * Returns the canonical OAuth2 scope representation (resource:action).
     */
    public String asScope() {
        if (this.name != null && !this.name.isBlank()) {
            return this.name;
        }
        String normalizedResource = this.resource == null ? "" : this.resource.trim();
        String normalizedAction = this.action == null ? "" : this.action.trim();
        if (normalizedResource.isEmpty() && normalizedAction.isEmpty()) {
            return "";
        }
        return normalizedResource + ":" + normalizedAction;
    }

    /**
     * Converts this entity to a Permission record for data transfer.
     *
     * @return a Permission record representing this entity's data
     */
    public Permission toRecord() {
        return new Permission(id, name, description, resource, action, active, createdAt, updatedAt);
    }

    /**
     * Creates a PermissionEntity from a Permission record.
     * This is a public factory method to allow creation from outside the domain package.
     *
     * @param permissionRecord the permission record
     * @return the permission entity
     */
    public static PermissionEntity fromRecord(Permission permissionRecord) {
        return new PermissionEntity(permissionRecord);
    }

}
