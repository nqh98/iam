package com.huynq.iam.core.domain.entity;


import com.huynq.iam.core.domain.entity.record.User;

import java.util.HashSet;
import java.util.Set;

/**
 * User entity representing a user in the identity and access management system.
 * This class is immutable and thread-safe.
 * Use {@link User} to create instances.
 */
public class UserEntity {
    private final Long id;
    private final String password;
    private final Long createdAt;
    private final Long updatedAt;
    private final Set<Long> roleIds;
    private final String externalId;

    // Package-private constructor - use builder
    UserEntity(User user) {
        this.id = user.id();
        this.password = user.password();
        this.createdAt = user.createdAt();
        this.updatedAt = user.updatedAt();
        this.roleIds = Set.copyOf(user.roleIds());
        this.externalId = user.externalId();
    }

    // Getters only (immutable)
    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public String getExternalId() {
        return externalId;
    }

    public UserEntity withRole(Long roleId) {
        Set<Long> newRoles = new HashSet<>(this.roleIds);
        if (!newRoles.add(roleId)) {
            return this;
        }
        User updatedUser = User.builder()
                .from(this.toRecord())
                .setRoleIds(newRoles)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new UserEntity(updatedUser);
    }

    public UserEntity withoutRole(Long roleId) {
        Set<Long> newRoles = new HashSet<>(this.roleIds);
        if (!newRoles.remove(roleId)) {
            return this;
        }
        User updatedUser = User.builder()
                .from(this.toRecord())
                .setRoleIds(newRoles)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new UserEntity(updatedUser);
    }

    public UserEntity withPassword(String newPassword) {
        User updatedUser = User.builder()
                .from(this.toRecord())
                .setPassword(newPassword)
                .setUpdatedAt(System.currentTimeMillis())
                .build();
        return new UserEntity(updatedUser);
    }

    public boolean hasRole(Long roleId) {
        return this.roleIds.contains(roleId);
    }

    /**
     * Converts this entity to a User record for data transfer.
     *
     * @return a User record representing this entity's data
     */
    public User toRecord() {
        return new User(id, password, createdAt, updatedAt, roleIds, externalId);
    }

    /**
     * Creates a UserEntity from a User record.
     * This is a public factory method to allow creation from outside the domain package.
     *
     * @param userRecord the user record
     * @return the user entity
     */
    public static UserEntity fromRecord(User userRecord) {
        return new UserEntity(userRecord);
    }

}
