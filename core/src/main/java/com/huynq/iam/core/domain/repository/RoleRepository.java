package com.huynq.iam.core.domain.repository;

import com.huynq.iam.core.domain.entity.RoleEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository {
    RoleEntity save(RoleEntity role);

    Optional<RoleEntity> findById(Long id);

    Optional<RoleEntity> findByName(String name);

    boolean existsById(long roleId);

    boolean existsByName(String name);

    void deleteById(Long roleId);

    List<RoleEntity> findAll();

    Set<RoleEntity> findByIds(Set<Long> roleIds);
}
