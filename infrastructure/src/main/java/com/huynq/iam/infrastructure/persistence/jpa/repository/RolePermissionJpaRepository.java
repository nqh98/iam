package com.huynq.iam.infrastructure.persistence.jpa.repository;

import com.huynq.iam.infrastructure.persistence.jpa.entity.RolePermissionJpaEntity;
import com.huynq.iam.infrastructure.persistence.jpa.entity.RolePermissionKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RolePermissionJpaRepository extends JpaRepository<RolePermissionJpaEntity, RolePermissionKey> {

    Set<RolePermissionJpaEntity> findByIdRoleId(Long roleId);

    Set<RolePermissionJpaEntity> findByIdRoleIdIn(Set<Long> roleIds);

    void deleteByIdRoleId(Long roleId);
}

