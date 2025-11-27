package com.huynq.iam.infrastructure.api;

import com.huynq.application.port.in.LoginUseCase;
import com.huynq.application.port.in.TokenRefreshUseCase;
import com.huynq.application.port.in.command.LoginCommand;
import com.huynq.application.port.in.command.RefreshTokenCommand;
import com.huynq.application.port.in.command.RevokeRefreshTokenCommand;
import com.huynq.application.port.in.result.LoginResult;
import com.huynq.iam.core.domain.exception.BusinessException;
import com.huynq.iam.infrastructure.api.dto.LoginRequest;
import com.huynq.iam.infrastructure.api.dto.LogoutRequest;
import com.huynq.iam.infrastructure.api.dto.RefreshTokenRequest;
import com.huynq.iam.infrastructure.api.dto.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final TokenRefreshUseCase tokenRefreshUseCase;

    public AuthController(LoginUseCase loginUseCase,
                          TokenRefreshUseCase tokenRefreshUseCase) {
        this.loginUseCase = loginUseCase;
        this.tokenRefreshUseCase = tokenRefreshUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletRequest servletRequest) throws BusinessException {
        LoginResult result = loginUseCase.login(LoginCommand.of(
                request.externalId(),
                request.password(),
                request.clientId(),
                resolveUserAgent(request.userAgent(), servletRequest),
                resolveIp(null, servletRequest)
        ));
        return ResponseEntity.ok(toResponse(result));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request,
                                                 HttpServletRequest servletRequest) throws BusinessException {
        String ipAddress = resolveIp(request.ipAddress(), servletRequest);
        LoginResult result = tokenRefreshUseCase.refresh(RefreshTokenCommand.of(
                request.refreshToken(),
                request.clientId(),
                resolveUserAgent(request.userAgent(), servletRequest),
                ipAddress
        ));
        return ResponseEntity.ok(toResponse(result));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) throws BusinessException {
        tokenRefreshUseCase.revoke(RevokeRefreshTokenCommand.of(
                request.refreshToken(),
                request.reason()
        ));
        return ResponseEntity.noContent().build();
    }

    private TokenResponse toResponse(LoginResult result) {
        return new TokenResponse(
                result.userId(),
                result.externalId(),
                result.roles(),
                result.scopes(),
                result.tokenType(),
                result.accessToken(),
                result.refreshToken(),
                result.accessTokenExpiresAt(),
                result.refreshTokenExpiresAt(),
                result.sessionId()
        );
    }

    private String resolveIp(String overrideIp, HttpServletRequest request) {
        if (overrideIp != null && !overrideIp.isBlank()) {
            return overrideIp;
        }
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String resolveUserAgent(String overrideUserAgent, HttpServletRequest request) {
        if (overrideUserAgent != null && !overrideUserAgent.isBlank()) {
            return overrideUserAgent;
        }
        return request.getHeader("User-Agent");
    }
}

