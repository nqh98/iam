package com.huynq.iam.infrastructure.service;


import com.huynq.iam.core.domain.service.PasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Implementation of PasswordService for the infrastructure layer.
 */
public class PasswordServiceImpl implements PasswordService {

    private final PasswordEncoder passwordEncoder;

    public PasswordServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    @Override
    public boolean matches(String plainPassword, String encodedPassword) {
        return passwordEncoder.matches(plainPassword, encodedPassword);
    }
}
