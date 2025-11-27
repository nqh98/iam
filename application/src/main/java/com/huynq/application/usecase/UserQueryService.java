package com.huynq.application.usecase;

import com.huynq.application.port.in.UserQueryUseCase;
import com.huynq.application.port.in.result.RoleDetailsResult;
import com.huynq.application.port.in.result.UserDetailsResult;
import com.huynq.iam.core.domain.entity.PermissionEntity;
import com.huynq.iam.core.domain.entity.RoleEntity;
import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.enums.ErrorCode;
import com.huynq.iam.core.domain.exception.BusinessException;
import com.huynq.iam.core.domain.repository.RoleRepository;
import com.huynq.iam.core.domain.repository.UserRepository;
import com.huynq.iam.core.domain.service.PermissionResolver;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Projection-oriented use case for retrieving rich user details.
 */
public class UserQueryService implements UserQueryUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionResolver permissionResolver;

    public UserQueryService(UserRepository userRepository,
                            RoleRepository roleRepository,
                            PermissionResolver permissionResolver) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionResolver = permissionResolver;
    }

    @Override
    public UserDetailsResult getUser(Long userId) throws BusinessException {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getDefaultMessage()
        ));
        return toResult(user);
    }

    @Override
    public List<UserDetailsResult> listUsers() {
        List<UserEntity> users = userRepository.findAll();
        if (users == null || users.isEmpty()) {
            return List.of();
        }
        return users.stream()
                .map(this::toResult)
                .toList();
    }

    private UserDetailsResult toResult(UserEntity user) {
        Set<RoleEntity> roles = roleRepository.findByIds(user.getRoleIds());
        List<RoleDetailsResult> roleDetails = roles == null
                ? List.of()
                : roles.stream()
                .map(this::toRoleResult)
                .toList();
        Set<String> scopes = permissionResolver.resolvePermissionsForUser(user.getId());
        return new UserDetailsResult(
                user.getId(),
                user.getExternalId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                roleDetails,
                scopes
        );
    }

    private RoleDetailsResult toRoleResult(RoleEntity role) {
        List<String> permissionScopes = role.getPermissions() == null
                ? List.of()
                : role.getPermissions().stream()
                .map(PermissionEntity::asScope)
                .filter(Objects::nonNull)
                .sorted()
                .toList();
        return new RoleDetailsResult(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.isActive(),
                permissionScopes,
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }
}

