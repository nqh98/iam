package com.huynq.iam.infrastructure.persistence;

import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.entity.UserRoleEntity;
import com.huynq.iam.core.domain.entity.record.User;
import com.huynq.iam.core.domain.entity.record.UserRole;
import com.huynq.iam.core.domain.repository.UserRepository;
import com.huynq.iam.core.domain.repository.UserRoleRepository;
import com.huynq.iam.infrastructure.persistence.jpa.entity.UserJpaEntity;
import com.huynq.iam.infrastructure.persistence.jpa.entity.UserRoleJpaEntity;
import com.huynq.iam.infrastructure.persistence.jpa.entity.UserRoleKey;
import com.huynq.iam.infrastructure.persistence.jpa.repository.UserJpaRepository;
import com.huynq.iam.infrastructure.persistence.jpa.repository.UserRoleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserPersistenceAdapter implements UserRepository, UserRoleRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository,
                                  UserRoleJpaRepository userRoleJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.userRoleJpaRepository = userRoleJpaRepository;
    }

    @Override
    @Transactional
    public UserEntity save(UserEntity user) {
        UserJpaEntity entity = UserJpaEntity.fromDomain(user);
        userJpaRepository.save(entity);
        syncRoles(user.getId(), user.getRoleIds());
        return user;
    }

    @Override
    public boolean existsByExternalId(String externalId) {
        return userJpaRepository.existsByExternalId(externalId);
    }

    @Override
    public Optional<UserEntity> findByExternalId(String externalId) {
        return userJpaRepository.findByExternalId(externalId)
                .map(user -> toDomain(user, userRoleJpaRepository.findByIdUserId(user.getId())));
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(user -> toDomain(user, userRoleJpaRepository.findByIdUserId(user.getId())));
    }

    @Override
    public boolean existsById(Long id) {
        return userJpaRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Set<Long> roleIds = rolesForUser(id);
        if (!roleIds.isEmpty()) {
            userRoleJpaRepository.deleteByUserIdAndRoleIds(id, roleIds);
        }
        userJpaRepository.deleteById(id);
    }

    @Override
    public List<UserEntity> findAll() {
        return userJpaRepository.findAll().stream()
                .map(user -> toDomain(user, userRoleJpaRepository.findByIdUserId(user.getId())))
                .toList();
    }

    @Override
    public Set<UserRoleEntity> findByUserId(Long userId) {
        return userRoleJpaRepository.findByIdUserId(userId).stream()
                .map(jpa -> UserRoleEntity.fromRecord(jpa.toRecord()))
                .collect(Collectors.toUnmodifiableSet());
    }

    private void syncRoles(Long userId, Set<Long> desiredRoleIds) {
        Set<Long> target = desiredRoleIds == null ? Set.of() : desiredRoleIds;
        Set<UserRoleJpaEntity> current = userRoleJpaRepository.findByIdUserId(userId);
        Set<Long> currentIds = current.stream()
                .map(entry -> entry.getId().getRoleId())
                .collect(Collectors.toSet());

        Set<Long> toAdd = new HashSet<>(target);
        toAdd.removeAll(currentIds);

        Set<Long> toRemove = new HashSet<>(currentIds);
        toRemove.removeAll(target);

        long now = System.currentTimeMillis();
        for (Long roleId : toAdd) {
            userRoleJpaRepository.save(new UserRoleJpaEntity(
                    new UserRoleKey(userId, roleId),
                    now
            ));
        }
        if (!toRemove.isEmpty()) {
            userRoleJpaRepository.deleteByUserIdAndRoleIds(userId, toRemove);
        }
    }

    private Set<Long> rolesForUser(Long userId) {
        return userRoleJpaRepository.findByIdUserId(userId).stream()
                .map(entry -> entry.getId().getRoleId())
                .collect(Collectors.toSet());
    }

    private UserEntity toDomain(UserJpaEntity entity, Set<UserRoleJpaEntity> roleEntries) {
        Set<Long> roleIds = roleEntries == null ? Set.of() : roleEntries.stream()
                .map(jpa -> jpa.getId().getRoleId())
                .collect(Collectors.toUnmodifiableSet());
        User record = entity.toRecord();
        record = User.builder()
                .from(record)
                .setRoleIds(roleIds)
                .build();
        return UserEntity.fromRecord(record);
    }

    private UserRoleEntity toDomain(UserRoleJpaEntity entity) {
        UserRole record = entity.toRecord();
        return UserRoleEntity.fromRecord(record);
    }
}

