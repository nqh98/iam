package com.huynq.iam.infrastructure.persistence.jpa.entity;

import com.huynq.iam.core.domain.entity.UserPermissionEntity;
import com.huynq.iam.core.domain.entity.record.UserPermission;
import com.huynq.iam.core.domain.enums.UserPermissionOverrideType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "iam_user_permissions")
public class UserPermissionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "permission_id", nullable = false)
    private Long permissionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "override_type", nullable = false)
    private UserPermissionOverrideType overrideType;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;

    protected UserPermissionJpaEntity() {
    }

    public UserPermissionJpaEntity(Long id,
                                   Long userId,
                                   Long permissionId,
                                   UserPermissionOverrideType overrideType,
                                   Long createdAt,
                                   Long updatedAt) {
        this.id = id;
        this.userId = userId;
        this.permissionId = permissionId;
        this.overrideType = overrideType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserPermissionJpaEntity fromDomain(UserPermissionEntity entity) {
        return new UserPermissionJpaEntity(
                entity.getId(),
                entity.getUserId(),
                entity.getPermissionId(),
                entity.getOverrideType(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public UserPermission toRecord() {
        return UserPermission.builder()
                .setId(id)
                .setUserId(userId)
                .setPermissionId(permissionId)
                .setOverrideType(overrideType)
                .setCreatedAt(createdAt)
                .setUpdatedAt(updatedAt)
                .build();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public UserPermissionOverrideType getOverrideType() {
        return overrideType;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public void setOverrideType(UserPermissionOverrideType overrideType) {
        this.overrideType = overrideType;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}

