package com.huynq.iam.infrastructure.persistence.jpa.repository;

import com.huynq.iam.infrastructure.persistence.jpa.entity.UserPermissionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserPermissionJpaRepository extends JpaRepository<UserPermissionJpaEntity, Long> {

    Set<UserPermissionJpaEntity> findByUserId(Long userId);
}

