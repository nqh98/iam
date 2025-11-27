package com.huynq.iam.core.domain.entity;

import com.huynq.iam.core.domain.entity.record.RefreshToken;

import java.util.Objects;
import java.util.Set;

/**
 * Immutable snapshot of a refresh token suitable for business rules.
 */
public class RefreshTokenEntity {
    private final Long id;
    private final String tokenId;
    private final Long userId;
    private final String sessionId;
    private final String hashedValue;
    private final String clientId;
    private final String userAgent;
    private final String ipAddress;
    private final Long issuedAt;
    private final Long expiresAt;
    private final Long revokedAt;
    private final String revokedReason;
    private final String replacedByTokenId;
    private final Set<String> scopes;

    RefreshTokenEntity(RefreshToken refreshToken) {
        this.id = refreshToken.id();
        this.tokenId = refreshToken.tokenId();
        this.userId = refreshToken.userId();
        this.sessionId = refreshToken.sessionId();
        this.hashedValue = refreshToken.hashedValue();
        this.clientId = refreshToken.clientId();
        this.userAgent = refreshToken.userAgent();
        this.ipAddress = refreshToken.ipAddress();
        this.issuedAt = refreshToken.issuedAt();
        this.expiresAt = refreshToken.expiresAt();
        this.revokedAt = refreshToken.revokedAt();
        this.revokedReason = refreshToken.revokedReason();
        this.replacedByTokenId = refreshToken.replacedByTokenId();
        this.scopes = refreshToken.scopes();
    }

    public Long getId() {
        return id;
    }

    public String getTokenId() {
        return tokenId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getHashedValue() {
        return hashedValue;
    }

    public String getClientId() {
        return clientId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Long getIssuedAt() {
        return issuedAt;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public Long getRevokedAt() {
        return revokedAt;
    }

    public String getRevokedReason() {
        return revokedReason;
    }

    public String getReplacedByTokenId() {
        return replacedByTokenId;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public boolean isExpired(long nowEpochMillis) {
        return expiresAt != null && expiresAt <= nowEpochMillis;
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public boolean matchesHashedValue(String hashed) {
        return Objects.equals(this.hashedValue, hashed);
    }

    public RefreshTokenEntity revoke(String reason, long revokedAtMillis, String replacementTokenId) {
        RefreshToken.Builder builder = RefreshToken.builder()
                .from(this.toRecord())
                .setRevokedAt(revokedAtMillis)
                .setRevokedReason(reason)
                .setReplacedByTokenId(replacementTokenId);
        return new RefreshTokenEntity(builder.build());
    }

    public RefreshToken toRecord() {
        return new RefreshToken(
                id,
                tokenId,
                userId,
                sessionId,
                hashedValue,
                clientId,
                userAgent,
                ipAddress,
                issuedAt,
                expiresAt,
                revokedAt,
                revokedReason,
                replacedByTokenId,
                scopes
        );
    }

    public static RefreshTokenEntity fromRecord(RefreshToken refreshToken) {
        return new RefreshTokenEntity(refreshToken);
    }
}

