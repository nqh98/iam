package com.huynq.iam.infrastructure.service;

import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.enums.ErrorCode;
import com.huynq.iam.core.domain.exception.BusinessException;
import com.huynq.iam.core.domain.repository.UserRepository;
import com.huynq.iam.core.domain.service.AuthenticationService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity authenticate(long id, String password) throws BusinessException {
        // Find user by id
        UserEntity user = userRepository.findById(id).orElseThrow(() ->
                new BusinessException(
                        ErrorCode.INVALID_CREDENTIALS.getCode(),
                        ErrorCode.INVALID_CREDENTIALS.getDefaultMessage()
                )
        );

        // Check password using PasswordEncoder against stored hash
        boolean matches = passwordEncoder.matches(password, user.getPassword());

        if (!matches) {
            throw new BusinessException(
                    ErrorCode.INVALID_CREDENTIALS.getCode(),
                    ErrorCode.INVALID_CREDENTIALS.getDefaultMessage()
            );
        }

        return user;
    }
}
