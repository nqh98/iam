package com.huynq.iam.infrastructure.persistence.jpa.entity;

import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.entity.record.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA representation of IAM users table.
 */
@Entity
@Table(name = "iam_users")
public class UserJpaEntity {

    @Id
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true, length = 150)
    private String externalId;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;

    protected UserJpaEntity() {
        // for JPA
    }

    public UserJpaEntity(Long id, String externalId, String passwordHash, Long createdAt, Long updatedAt) {
        this.id = id;
        this.externalId = externalId;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserJpaEntity fromDomain(UserEntity userEntity) {
        return new UserJpaEntity(
                userEntity.getId(),
                userEntity.getExternalId(),
                userEntity.getPassword(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt()
        );
    }

    public User toRecord() {
        return User.builder()
                .setId(id)
                .setExternalId(externalId)
                .setPassword(passwordHash)
                .setCreatedAt(createdAt)
                .setUpdatedAt(updatedAt)
                .build();
    }

    public Long getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getPasswordHash() {
        return passwordHash;
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

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}

