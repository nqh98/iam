package com.huynq.application.usecase;

import com.huynq.application.dto.LoginRequest;
import com.huynq.application.dto.LoginResponse;
import com.huynq.iam.core.domain.service.AuthenticationService;

public class AuthenticationUseCase {
    private final AuthenticationService authenticationService;

    public AuthenticationUseCase(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public LoginResponse login(LoginRequest request) {

    }
}
