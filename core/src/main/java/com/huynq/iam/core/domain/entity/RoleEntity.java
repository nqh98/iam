package com.huynq.iam.core.domain.entity;


import com.huynq.iam.core.domain.entity.record.Role;

import java.util.HashSet;
import java.util.Set;

/**
 * Role entity representing a role in the identity and access management system.
 * This class is immutable and thread-safe.
 * Use {@link Role} to create instances.
 */
public class RoleEntity {
    private final Long id;
    private final String name;
    private final String description;
    private final boolean active;
    private final Long createdAt;
    private final Long updatedAt;
    private final Set<PermissionEntity> permissionEntities;

    // Package-private constructor - use builder
    RoleEntity(Role role) {
        this.id = role.id();
        this.name = role.name();
        this.description = role.description();
        this.active = role.active();
        this.createdAt = role.createdAt();
        this.updatedAt = role.updatedAt();
        this.permissionEntities = role.permissionEntities();
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

    public boolean isActive() {
        return active;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public Set<PermissionEntity> getPermissions() {
        return permissionEntities; // Already unmodifiable
    }

    // Business methods - return new instances (immutable)
    public RoleEntity activate() {
        Role updatedRole = Role.builder()
                .from(this.toRecord())
                .setActive(true)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new RoleEntity(updatedRole);
    }

    public RoleEntity deactivate() {
        Role updatedRole = Role.builder()
                .from(this.toRecord())
                .setActive(false)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new RoleEntity(updatedRole);
    }

    public RoleEntity withPermission(PermissionEntity permission) {
        Set<PermissionEntity> newPermissions = new HashSet<>(this.permissionEntities);
        newPermissions.add(permission);
        Role updatedRole = Role.builder()
                .from(this.toRecord())
                .setPermissions(newPermissions)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new RoleEntity(updatedRole);
    }

    public RoleEntity withoutPermission(PermissionEntity permission) {
        Set<PermissionEntity> newPermissions = new HashSet<>(this.permissionEntities);
        newPermissions.remove(permission);
        Role updatedRole = Role.builder()
                .from(this.toRecord())
                .setPermissions(newPermissions)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new RoleEntity(updatedRole);
    }

    public boolean hasPermission(String permissionName) {
        return this.permissionEntities.stream().anyMatch(permission -> permission.getName().equals(permissionName));
    }

    public boolean hasPermission(PermissionEntity permission) {
        return this.permissionEntities.contains(permission);
    }

    public RoleEntity withDescription(String newDescription) {
        Role updatedRole = Role.builder()
                .from(this.toRecord())
                .setDescription(newDescription)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new RoleEntity(updatedRole);
    }

    public RoleEntity withName(String newName) {
        Role updatedRole = Role.builder()
                .from(this.toRecord())
                .setName(newName)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new RoleEntity(updatedRole);
    }

    public RoleEntity withPermissions(Set<PermissionEntity> permissions) {
        Set<PermissionEntity> copy = permissions == null ? Set.of() : Set.copyOf(permissions);
        Role updatedRole = Role.builder()
                .from(this.toRecord())
                .setPermissions(copy)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new RoleEntity(updatedRole);
    }

    /**
     * Converts this entity to a Role record for data transfer.
     *
     * @return a Role record representing this entity's data
     */
    public Role toRecord() {
        return new Role(id, name, description, active, createdAt, updatedAt, permissionEntities);
    }

    /**
     * Creates a RoleEntity from a Role record.
     * This is a public factory method to allow creation from outside the domain package.
     *
     * @param roleRecord the role record
     * @return the role entity
     */
    public static RoleEntity fromRecord(Role roleRecord) {
        return new RoleEntity(roleRecord);
    }

}
