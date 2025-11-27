package com.huynq.iam.core.domain.entity.record;

import java.util.Set;

/**
 * Persistence-agnostic data carrier describing a refresh token row.
 */
public record RefreshToken(
        Long id,
        String tokenId,
        Long userId,
        String sessionId,
        String hashedValue,
        String clientId,
        String userAgent,
        String ipAddress,
        Long issuedAt,
        Long expiresAt,
        Long revokedAt,
        String revokedReason,
        String replacedByTokenId,
        Set<String> scopes
) {

    public static class Builder {
        private Long id;
        private String tokenId;
        private Long userId;
        private String sessionId;
        private String hashedValue;
        private String clientId;
        private String userAgent;
        private String ipAddress;
        private Long issuedAt;
        private Long expiresAt;
        private Long revokedAt;
        private String revokedReason;
        private String replacedByTokenId;
        private Set<String> scopes = Set.of();

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setTokenId(String tokenId) {
            this.tokenId = tokenId;
            return this;
        }

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setSessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder setHashedValue(String hashedValue) {
            this.hashedValue = hashedValue;
            return this;
        }

        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder setUserAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder setIssuedAt(Long issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public Builder setExpiresAt(Long expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder setRevokedAt(Long revokedAt) {
            this.revokedAt = revokedAt;
            return this;
        }

        public Builder setRevokedReason(String revokedReason) {
            this.revokedReason = revokedReason;
            return this;
        }

        public Builder setReplacedByTokenId(String replacedByTokenId) {
            this.replacedByTokenId = replacedByTokenId;
            return this;
        }

        public Builder setScopes(Set<String> scopes) {
            this.scopes = scopes == null ? Set.of() : Set.copyOf(scopes);
            return this;
        }

        public Builder from(RefreshToken refreshToken) {
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
            return this;
        }

        public RefreshToken build() {
            Set<String> scopeSnapshot = scopes == null ? Set.of() : Set.copyOf(scopes);
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
                    scopeSnapshot
            );
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

