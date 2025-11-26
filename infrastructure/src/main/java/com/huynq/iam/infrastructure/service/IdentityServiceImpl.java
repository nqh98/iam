package com.huynq.iam.infrastructure.service;

import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.entity.record.User;
import com.huynq.iam.core.domain.enums.ErrorCode;
import com.huynq.iam.core.domain.exception.BusinessException;
import com.huynq.iam.core.domain.repository.RoleRepository;
import com.huynq.iam.core.domain.repository.UserRepository;
import com.huynq.iam.core.domain.service.IdentityService;
import com.huynq.iam.core.domain.service.PasswordService;
import com.huynq.snowid.SnowflakeIdGenerator;
import org.springframework.transaction.annotation.Transactional;

public class IdentityServiceImpl implements IdentityService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final PasswordService passwordService;

    public IdentityServiceImpl(UserRepository userRepository,
                               RoleRepository roleRepository,
                               SnowflakeIdGenerator snowflakeIdGenerator,
                               PasswordService passwordService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.snowflakeIdGenerator = snowflakeIdGenerator;
        this.passwordService = passwordService;
    }

    @Override
    @Transactional
    public UserEntity createUser(String password, String externalId) throws BusinessException {
        if (userRepository.existsByExternalId(externalId)) {
            throw new BusinessException(
                    ErrorCode.EXTERNAL_ID_EXISTS.getCode(),
                    ErrorCode.EXTERNAL_ID_EXISTS.getDefaultMessage()
            );
        }

        // Create user with generated ID and encoded password
        String encodedPassword = passwordService.encode(password);
        long userId = snowflakeIdGenerator.nextId();
        long currentTimeMillis = System.currentTimeMillis();
        User userRecord = User.builder()
                .setId(userId)
                .setPassword(encodedPassword)
                .setExternalId(externalId)
                .setCreatedAt(currentTimeMillis)
                .setUpdatedAt(currentTimeMillis)
                .build();

        UserEntity user = UserEntity.fromRecord(userRecord);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserEntity assignRole(long userId, long roleId) throws BusinessException {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getDefaultMessage()
        ));

        if (user.hasRole(roleId)) {
            return user; // No change needed
        }

        if (!roleRepository.existsById(roleId)) {
            throw new BusinessException(
                    ErrorCode.ROLE_NOT_FOUND.getCode(),
                    ErrorCode.ROLE_NOT_FOUND.getDefaultMessage()
            );
        }

        UserEntity updated = user.withRole(roleId);
        return userRepository.save(updated);
    }

    @Override
    @Transactional
    public UserEntity removeRole(long userId, long roleId) throws BusinessException {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getDefaultMessage()
        ));

        // If the user doesn't have the role, no change is needed
        if (!user.hasRole(roleId)) {
            return user;
        }

        // Remove the role and persist the updated user
        UserEntity updated = user.withoutRole(roleId);
        return userRepository.save(updated);
    }

    @Override
    @Transactional
    public void changePassword(long userId, String oldPwd, String newPwd) throws BusinessException {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getDefaultMessage()
        ));

        // Verify old password matches current password using PasswordService
        if (!passwordService.matches(oldPwd, user.getPassword())) {
            throw new BusinessException(
                    ErrorCode.INVALID_OLD_PASSWORD.getCode(),
                    ErrorCode.INVALID_OLD_PASSWORD.getDefaultMessage()
            );
        }

        // Update password and persist
        String encodedNewPassword = passwordService.encode(newPwd);
        UserEntity updated = user.withPassword(encodedNewPassword);
        userRepository.save(updated);
    }
}
