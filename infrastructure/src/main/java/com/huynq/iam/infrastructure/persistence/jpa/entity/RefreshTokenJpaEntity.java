package com.huynq.iam.infrastructure.persistence.jpa.entity;

import com.huynq.iam.core.domain.entity.RefreshTokenEntity;
import com.huynq.iam.core.domain.entity.record.RefreshToken;
import com.huynq.iam.infrastructure.persistence.jpa.converter.StringSetConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "iam_refresh_tokens")
public class RefreshTokenJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_id", nullable = false, unique = true, length = 64)
    private String tokenId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "session_id", nullable = false, length = 64)
    private String sessionId;

    @Column(name = "hashed_value", nullable = false, length = 128)
    private String hashedValue;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "issued_at", nullable = false)
    private Long issuedAt;

    @Column(name = "expires_at", nullable = false)
    private Long expiresAt;

    @Column(name = "revoked_at")
    private Long revokedAt;

    @Column(name = "revoked_reason")
    private String revokedReason;

    @Column(name = "replaced_by_token_id")
    private String replacedByTokenId;

    @Convert(converter = StringSetConverter.class)
    @Column(name = "scopes", columnDefinition = "text")
    private Set<String> scopes;

    protected RefreshTokenJpaEntity() {
    }

    public RefreshTokenJpaEntity(Long id,
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
                                 Set<String> scopes) {
        this.id = id;
        this.tokenId = tokenId;
        this.userId = userId;
        this.sessionId = sessionId;
        this.hashedValue = hashedValue;
        this.clientId = clientId;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
        this.revokedReason = revokedReason;
        this.replacedByTokenId = replacedByTokenId;
        this.scopes = scopes;
    }

    public static RefreshTokenJpaEntity fromDomain(RefreshTokenEntity entity) {
        RefreshToken record = entity.toRecord();
        return new RefreshTokenJpaEntity(
                record.id(),
                record.tokenId(),
                record.userId(),
                record.sessionId(),
                record.hashedValue(),
                record.clientId(),
                record.userAgent(),
                record.ipAddress(),
                record.issuedAt(),
                record.expiresAt(),
                record.revokedAt(),
                record.revokedReason(),
                record.replacedByTokenId(),
                record.scopes()
        );
    }

    public RefreshToken toRecord() {
        return RefreshToken.builder()
                .setId(id)
                .setTokenId(tokenId)
                .setUserId(userId)
                .setSessionId(sessionId)
                .setHashedValue(hashedValue)
                .setClientId(clientId)
                .setUserAgent(userAgent)
                .setIpAddress(ipAddress)
                .setIssuedAt(issuedAt)
                .setExpiresAt(expiresAt)
                .setRevokedAt(revokedAt)
                .setRevokedReason(revokedReason)
                .setReplacedByTokenId(replacedByTokenId)
                .setScopes(scopes)
                .build();
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setHashedValue(String hashedValue) {
        this.hashedValue = hashedValue;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setIssuedAt(Long issuedAt) {
        this.issuedAt = issuedAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setRevokedAt(Long revokedAt) {
        this.revokedAt = revokedAt;
    }

    public void setRevokedReason(String revokedReason) {
        this.revokedReason = revokedReason;
    }

    public void setReplacedByTokenId(String replacedByTokenId) {
        this.replacedByTokenId = replacedByTokenId;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }
}

