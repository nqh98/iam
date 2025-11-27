package com.huynq.application.usecase;

import com.huynq.application.port.in.LoginUseCase;
import com.huynq.application.port.in.TokenRefreshUseCase;
import com.huynq.application.port.in.command.LoginCommand;
import com.huynq.application.port.in.command.RefreshTokenCommand;
import com.huynq.application.port.in.command.RevokeRefreshTokenCommand;
import com.huynq.application.port.in.result.LoginResult;
import com.huynq.iam.core.domain.entity.RefreshTokenEntity;
import com.huynq.iam.core.domain.entity.RoleEntity;
import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.entity.record.RefreshToken;
import com.huynq.iam.core.domain.enums.ErrorCode;
import com.huynq.iam.core.domain.exception.BusinessException;
import com.huynq.iam.core.domain.repository.RefreshTokenRepository;
import com.huynq.iam.core.domain.repository.RoleRepository;
import com.huynq.iam.core.domain.repository.UserRepository;
import com.huynq.iam.core.domain.repository.UserRoleRepository;
import com.huynq.iam.core.domain.service.AuthenticationService;
import com.huynq.iam.core.domain.service.JwtTokenService;
import com.huynq.iam.core.domain.service.PermissionResolver;
import com.huynq.iam.core.domain.valueobject.RefreshTokenMetadata;
import com.huynq.iam.core.domain.valueobject.TokenPair;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Authentication interactor orchestrating credential verification,
 * permission resolution, and secure token lifecycle management.
 */
public class AuthenticationUseCase implements LoginUseCase, TokenRefreshUseCase {
    private static final HexFormat HEX_FORMAT = HexFormat.of();

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PermissionResolver permissionResolver;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationUseCase(AuthenticationService authenticationService,
                                 UserRepository userRepository,
                                 UserRoleRepository userRoleRepository,
                                 RoleRepository roleRepository,
                                 PermissionResolver permissionResolver,
                                 JwtTokenService jwtTokenService,
                                 RefreshTokenRepository refreshTokenRepository) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.permissionResolver = permissionResolver;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public LoginResult login(LoginCommand command) throws BusinessException {
        UserEntity user = authenticationService.authenticate(
                command.externalId(),
                command.password()
        );
        return issueTokensForUser(
                user,
                command.clientId(),
                command.userAgent(),
                command.ipAddress()
        ).result();
    }

    @Override
    public LoginResult refresh(RefreshTokenCommand command) throws BusinessException {
        RefreshTokenMetadata metadata = decodeRefreshToken(command.refreshToken());
        long now = System.currentTimeMillis();
        if (metadata.expiresAt() <= now) {
            throw new BusinessException(
                    ErrorCode.REFRESH_TOKEN_EXPIRED.getCode(),
                    ErrorCode.REFRESH_TOKEN_EXPIRED.getDefaultMessage()
            );
        }

        RefreshTokenEntity stored = refreshTokenRepository.findByTokenId(metadata.tokenId())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.REFRESH_TOKEN_INVALID.getCode(),
                        ErrorCode.REFRESH_TOKEN_INVALID.getDefaultMessage()
                ));

        if (stored.isRevoked()) {
            throw new BusinessException(
                    ErrorCode.REFRESH_TOKEN_REVOKED.getCode(),
                    ErrorCode.REFRESH_TOKEN_REVOKED.getDefaultMessage()
            );
        }
        if (stored.isExpired(now)) {
            throw new BusinessException(
                    ErrorCode.REFRESH_TOKEN_EXPIRED.getCode(),
                    ErrorCode.REFRESH_TOKEN_EXPIRED.getDefaultMessage()
            );
        }

        String incomingHash = hashToken(command.refreshToken());
        if (!stored.matchesHashedValue(incomingHash)) {
            RefreshTokenEntity revoked = stored.revoke(
                    "hash-mismatch",
                    now,
                    null
            );
            refreshTokenRepository.save(revoked);
            throw new BusinessException(
                    ErrorCode.REFRESH_TOKEN_REUSE_DETECTED.getCode(),
                    ErrorCode.REFRESH_TOKEN_REUSE_DETECTED.getDefaultMessage()
            );
        }

        UserEntity user = userRepository.findById(stored.getUserId()).orElseThrow(() -> new BusinessException(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getDefaultMessage()
        ));

        String clientId = command.clientId() == null ? stored.getClientId() : command.clientId();
        String userAgent = command.userAgent() == null ? stored.getUserAgent() : command.userAgent();
        String ipAddress = command.ipAddress() == null ? stored.getIpAddress() : command.ipAddress();
        TokenIssuance issuance = issueTokensForUser(
                user,
                clientId,
                userAgent,
                ipAddress
        );

        long rotationTime = System.currentTimeMillis();
        RefreshTokenEntity revoked = stored.revoke(
                "rotated",
                rotationTime,
                issuance.tokenPair().refreshTokenId()
        );
        refreshTokenRepository.save(revoked);

        return issuance.result();
    }

    @Override
    public void revoke(RevokeRefreshTokenCommand command) throws BusinessException {
        RefreshTokenMetadata metadata = decodeRefreshToken(command.refreshToken());
        RefreshTokenEntity stored = refreshTokenRepository.findByTokenId(metadata.tokenId())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.REFRESH_TOKEN_INVALID.getCode(),
                        ErrorCode.REFRESH_TOKEN_INVALID.getDefaultMessage()
                ));
        long now = System.currentTimeMillis();
        RefreshTokenEntity revoked = stored.revoke(
                command.reason() == null ? "logout" : command.reason(),
                now,
                null
        );
        refreshTokenRepository.save(revoked);
        refreshTokenRepository.deleteBySessionId(metadata.sessionId());
    }

    private TokenIssuance issueTokensForUser(UserEntity user,
                                             String clientId,
                                             String userAgent,
                                             String ipAddress) {
        Set<Long> roleIds = userRoleRepository.findRoleIdsByUserId(user.getId());
        List<String> roleNames = resolveRoleNames(roleIds);
        Set<String> scopes = permissionResolver.resolvePermissionsForUser(user.getId());
        TokenPair tokenPair = jwtTokenService.generateTokenPair(
                user.getId(),
                user.getExternalId(),
                roleNames,
                scopes
        );

        persistRefreshToken(user.getId(), tokenPair, scopes, clientId, userAgent, ipAddress);

        LoginResult result = new LoginResult(
                user.getId(),
                user.getExternalId(),
                roleNames,
                scopes,
                "Bearer",
                tokenPair.accessToken(),
                tokenPair.refreshToken(),
                tokenPair.accessTokenExpiresAt(),
                tokenPair.refreshTokenExpiresAt(),
                tokenPair.sessionId()
        );
        return new TokenIssuance(tokenPair, result);
    }

    private void persistRefreshToken(Long userId,
                                     TokenPair tokenPair,
                                     Set<String> scopes,
                                     String clientId,
                                     String userAgent,
                                     String ipAddress) {
        long now = System.currentTimeMillis();
        RefreshToken refreshToken = RefreshToken.builder()
                .setTokenId(tokenPair.refreshTokenId())
                .setUserId(userId)
                .setSessionId(tokenPair.sessionId())
                .setHashedValue(hashToken(tokenPair.refreshToken()))
                .setClientId(clientId)
                .setUserAgent(userAgent)
                .setIpAddress(ipAddress)
                .setIssuedAt(now)
                .setExpiresAt(tokenPair.refreshTokenExpiresAt())
                .setScopes(scopes)
                .build();
        refreshTokenRepository.save(RefreshTokenEntity.fromRecord(refreshToken));
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HEX_FORMAT.formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private RefreshTokenMetadata decodeRefreshToken(String token) throws BusinessException {
        try {
            return jwtTokenService.parseRefreshToken(token);
        } catch (RuntimeException ex) {
            throw new BusinessException(
                    ErrorCode.REFRESH_TOKEN_INVALID.getCode(),
                    ErrorCode.REFRESH_TOKEN_INVALID.getDefaultMessage(),
                    ex
            );
        }
    }

    private List<String> resolveRoleNames(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return List.of();
        }
        Set<RoleEntity> roleEntities = roleRepository.findByIds(roleIds);
        if (roleEntities == null || roleEntities.isEmpty()) {
            return Collections.emptyList();
        }
        return roleEntities.stream()
                .map(RoleEntity::getName)
                .filter(Objects::nonNull)
                .sorted()
                .toList();
    }

    private record TokenIssuance(TokenPair tokenPair, LoginResult result) { }
}
