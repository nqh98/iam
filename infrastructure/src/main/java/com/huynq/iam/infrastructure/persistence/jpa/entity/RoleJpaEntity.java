package com.huynq.iam.infrastructure.persistence.jpa.entity;

import com.huynq.iam.core.domain.entity.RoleEntity;
import com.huynq.iam.core.domain.entity.record.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "iam_roles")
public class RoleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;

    protected RoleJpaEntity() {
    }

    public RoleJpaEntity(Long id,
                         String name,
                         String description,
                         boolean active,
                         Long createdAt,
                         Long updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RoleJpaEntity fromDomain(RoleEntity roleEntity) {
        return new RoleJpaEntity(
                roleEntity.getId(),
                roleEntity.getName(),
                roleEntity.getDescription(),
                roleEntity.isActive(),
                roleEntity.getCreatedAt(),
                roleEntity.getUpdatedAt()
        );
    }

    public Role toRecord() {
        return Role.builder()
                .setId(id)
                .setName(name)
                .setDescription(description)
                .setActive(active)
                .setCreatedAt(createdAt)
                .setUpdatedAt(updatedAt)
                .build();
    }

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}

