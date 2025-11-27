package com.huynq.iam.infrastructure.persistence.jpa.repository;

import com.huynq.iam.infrastructure.persistence.jpa.entity.UserRoleJpaEntity;
import com.huynq.iam.infrastructure.persistence.jpa.entity.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserRoleJpaRepository extends JpaRepository<UserRoleJpaEntity, UserRoleKey> {

    Set<UserRoleJpaEntity> findByIdUserId(Long userId);

    @Modifying
    @Query("delete from UserRoleJpaEntity ur where ur.id.userId = :userId and ur.id.roleId in :roleIds")
    void deleteByUserIdAndRoleIds(@Param("userId") Long userId, @Param("roleIds") Set<Long> roleIds);

    @Modifying
    @Query("delete from UserRoleJpaEntity ur where ur.id.roleId = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);
}

