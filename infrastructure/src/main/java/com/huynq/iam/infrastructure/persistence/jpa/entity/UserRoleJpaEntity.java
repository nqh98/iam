package com.huynq.iam.infrastructure.persistence.jpa.entity;

import com.huynq.iam.core.domain.entity.UserRoleEntity;
import com.huynq.iam.core.domain.entity.record.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "iam_user_roles")
public class UserRoleJpaEntity {

    @EmbeddedId
    private UserRoleKey id;

    @Column(name = "assigned_at", nullable = false)
    private Long assignedAt;

    protected UserRoleJpaEntity() {
    }

    public UserRoleJpaEntity(UserRoleKey id, Long assignedAt) {
        this.id = id;
        this.assignedAt = assignedAt;
    }

    public static UserRoleJpaEntity fromDomain(UserRoleEntity entity) {
        return new UserRoleJpaEntity(
                new UserRoleKey(entity.getUserId(), entity.getRoleId()),
                entity.getAssignedAt()
        );
    }

    public UserRole toRecord() {
        return UserRole.builder()
                .setUserId(id.getUserId())
                .setRoleId(id.getRoleId())
                .setAssignedAt(assignedAt)
                .build();
    }

    public UserRoleKey getId() {
        return id;
    }

    public Long getAssignedAt() {
        return assignedAt;
    }

    public void setId(UserRoleKey id) {
        this.id = id;
    }

    public void setAssignedAt(Long assignedAt) {
        this.assignedAt = assignedAt;
    }
}

