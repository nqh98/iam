package com.huynq.iam.infrastructure.persistence.jpa.entity;

import com.huynq.iam.core.domain.entity.PermissionEntity;
import com.huynq.iam.core.domain.entity.record.Permission;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "iam_permissions")
public class PermissionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "resource", nullable = false, length = 150)
    private String resource;

    @Column(name = "action", nullable = false, length = 150)
    private String action;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;

    protected PermissionJpaEntity() {
    }

    public PermissionJpaEntity(Long id,
                               String name,
                               String description,
                               String resource,
                               String action,
                               boolean active,
                               Long createdAt,
                               Long updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.resource = resource;
        this.action = action;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PermissionJpaEntity fromDomain(PermissionEntity permission) {
        return new PermissionJpaEntity(
                permission.getId(),
                permission.getName(),
                permission.getDescription(),
                permission.getResource(),
                permission.getAction(),
                permission.isActive(),
                permission.getCreatedAt(),
                permission.getUpdatedAt()
        );
    }

    public Permission toRecord() {
        return Permission.builder()
                .setId(id)
                .setName(name)
                .setDescription(description)
                .setResource(resource)
                .setAction(action)
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setAction(String action) {
        this.action = action;
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

