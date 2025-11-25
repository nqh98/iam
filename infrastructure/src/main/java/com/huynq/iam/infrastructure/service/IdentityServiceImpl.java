package com.huynq.iam.infrastructure.service;

import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.entity.record.User;
import com.huynq.iam.core.domain.enums.ErrorCode;
import com.huynq.iam.core.domain.exception.BusinessException;
import com.huynq.iam.core.domain.repository.RoleRepository;
import com.huynq.iam.core.domain.repository.UserRepository;
import com.huynq.iam.core.domain.service.IdentityService;
import com.huynq.iam.core.domain.valueobject.Password;
import com.huynq.snowid.SnowflakeIdGenerator;
import org.springframework.transaction.annotation.Transactional;

public class IdentityServiceImpl implements IdentityService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    public IdentityServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                               SnowflakeIdGenerator snowflakeIdGenerator) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.snowflakeIdGenerator = snowflakeIdGenerator;
    }

    @Override
    @Transactional
    public UserEntity createUser(Password password, String externalId) throws BusinessException {
        if (userRepository.existsByExternalId(externalId)) {
            throw new BusinessException(
                    ErrorCode.EXTERNAL_ID_EXISTS.getCode(),
                    ErrorCode.EXTERNAL_ID_EXISTS.getDefaultMessage()
            );
        }

        // Create user with generated ID
        long userId = snowflakeIdGenerator.nextId();
        long currentTimeMillis = System.currentTimeMillis();
        User userRecord = User.builder()
                .setId(userId)
                .setPassword(password)
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
    public UserEntity removeRole(long userId, long roleId) {
        return null;
    }

    @Override
    @Transactional
    public boolean changePassword(long userId, Password oldPwd, Password newPwd) {
        return false;
    }
}
