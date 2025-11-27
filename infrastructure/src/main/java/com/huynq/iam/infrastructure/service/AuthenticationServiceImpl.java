package com.huynq.iam.infrastructure.service;

import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.enums.ErrorCode;
import com.huynq.iam.core.domain.exception.BusinessException;
import com.huynq.iam.core.domain.repository.UserRepository;
import com.huynq.iam.core.domain.service.AuthenticationService;
import com.huynq.iam.core.domain.service.PasswordService;

public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Override
    public UserEntity authenticate(String externalId, String password) throws BusinessException {
        UserEntity user = userRepository.findByExternalId(externalId).orElseThrow(() ->
                new BusinessException(
                        ErrorCode.INVALID_CREDENTIALS.getCode(),
                        ErrorCode.INVALID_CREDENTIALS.getDefaultMessage()
                )
        );

        if (!passwordService.matches(password, user.getPassword())) {
            throw new BusinessException(
                    ErrorCode.INVALID_CREDENTIALS.getCode(),
                    ErrorCode.INVALID_CREDENTIALS.getDefaultMessage()
            );
        }

        return user;
    }
}
