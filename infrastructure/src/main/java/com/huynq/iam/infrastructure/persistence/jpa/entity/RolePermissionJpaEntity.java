package com.huynq.iam.infrastructure.persistence.jpa.entity;

import com.huynq.iam.core.domain.entity.RolePermissionEntity;
import com.huynq.iam.core.domain.entity.record.RolePermission;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "iam_role_permissions")
public class RolePermissionJpaEntity {

    @EmbeddedId
    private RolePermissionKey id;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    protected RolePermissionJpaEntity() {
    }

    public RolePermissionJpaEntity(RolePermissionKey id, Long createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public static RolePermissionJpaEntity fromDomain(RolePermissionEntity entity) {
        return new RolePermissionJpaEntity(
                new RolePermissionKey(entity.getRoleId(), entity.getPermissionId()),
                entity.getCreatedAt()
        );
    }

    public RolePermission toRecord() {
        return RolePermission.builder()
                .setRoleId(id.getRoleId())
                .setPermissionId(id.getPermissionId())
                .setCreatedAt(createdAt)
                .build();
    }

    public RolePermissionKey getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setId(RolePermissionKey id) {
        this.id = id;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}

