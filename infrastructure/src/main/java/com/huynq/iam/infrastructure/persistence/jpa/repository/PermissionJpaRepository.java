package com.huynq.iam.infrastructure.persistence.jpa.repository;

import com.huynq.iam.infrastructure.persistence.jpa.entity.PermissionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface PermissionJpaRepository extends JpaRepository<PermissionJpaEntity, Long> {
    Optional<PermissionJpaEntity> findByNameIgnoreCase(String name);

    @Query("select p from PermissionJpaEntity p where lower(p.name) in :names")
    Set<PermissionJpaEntity> findByNameInIgnoreCase(@Param("names") Collection<String> names);

    Set<PermissionJpaEntity> findByNameIn(Collection<String> names);
}

