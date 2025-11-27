package com.huynq.iam.infrastructure.service;

import com.huynq.iam.core.domain.enums.TokenType;
import com.huynq.iam.core.domain.service.JwtTokenService;
import com.huynq.iam.core.domain.valueobject.RefreshTokenMetadata;
import com.huynq.iam.core.domain.valueobject.TokenPair;
import com.huynq.iam.infrastructure.security.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class JwtTokenServiceImpl implements JwtTokenService {
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;
    private final JwtParser jwtParser;

    public JwtTokenServiceImpl(JwtProperties jwtProperties, SecretKey secretKey) {
        this.jwtProperties = jwtProperties;
        this.secretKey = secretKey;
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    @Override
    public TokenPair generateTokenPair(long userId,
                                       String externalId,
                                       List<String> roles,
                                       Set<String> scopes) {
        String subject = externalId == null ? String.valueOf(userId) : externalId;
        String sessionId = UUID.randomUUID().toString();
        Instant issuedAt = Instant.now();
        Map<String, Object> accessClaims = new HashMap<>();
        accessClaims.put("roles", roles == null ? List.of() : roles);
        accessClaims.put("scope", formatScope(scopes));
        accessClaims.put("uid", userId);
        accessClaims.put("externalId", externalId);

        String accessTokenId = UUID.randomUUID().toString();
        String accessToken = buildToken(
                accessClaims,
                subject,
                sessionId,
                accessTokenId,
                TokenType.ACCESS,
                issuedAt,
                jwtProperties.getAccessTokenExpiration()
        );

        Map<String, Object> refreshClaims = new HashMap<>();
        refreshClaims.put("uid", userId);
        refreshClaims.put("externalId", externalId);
        refreshClaims.put("roles", roles == null ? List.of() : roles);
        refreshClaims.put("scope", formatScope(scopes));
        String refreshTokenId = UUID.randomUUID().toString();
        String refreshToken = buildToken(
                refreshClaims,
                subject,
                sessionId,
                refreshTokenId,
                TokenType.REFRESH,
                issuedAt,
                jwtProperties.getRefreshTokenExpiration()
        );

        long issuedAtMillis = issuedAt.toEpochMilli();
        return new TokenPair(
                sessionId,
                accessToken,
                refreshToken,
                issuedAtMillis + jwtProperties.getAccessTokenExpiration(),
                issuedAtMillis + jwtProperties.getRefreshTokenExpiration(),
                refreshTokenId
        );
    }
    @Override
    public RefreshTokenMetadata parseRefreshToken(String refreshToken) {
        Claims claims = jwtParser.parseSignedClaims(refreshToken).getPayload();
        String tokenId = claims.getId();
        String sessionId = claims.get("sid", String.class);
        Number uidNumber = claims.get("uid", Number.class);
        Long userId = uidNumber == null ? null : uidNumber.longValue();
        String externalId = claims.get("externalId", String.class);
        List<String> roles = toStringList(claims.get("roles", List.class));
        Set<String> scopes = parseScope(claims.get("scope", String.class));
        long expiresAt = claims.getExpiration() == null ? 0L : claims.getExpiration().getTime();
        return new RefreshTokenMetadata(
                tokenId,
                sessionId,
                userId,
                externalId,
                roles,
                scopes,
                expiresAt
        );
    }

    private String buildToken(Map<String, Object> additionalClaims,
                              String subject,
                              String sessionId,
                              String jwtId,
                              TokenType tokenType,
                              Instant issuedAt,
                              long ttlMillis) {
        Date issuedDate = Date.from(issuedAt);
        Date expiryDate = Date.from(issuedAt.plusMillis(ttlMillis));

        Map<String, Object> claims = new HashMap<>(additionalClaims);
        claims.put("sid", sessionId);
        claims.put("type", tokenType.getValue());

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer(jwtProperties.getIssuer())
                .id(jwtId)
                .issuedAt(issuedDate)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    private String formatScope(Set<String> scopes) {
        if (scopes == null || scopes.isEmpty()) {
            return "";
        }
        return scopes.stream()
                .filter(scope -> scope != null && !scope.isBlank())
                .sorted()
                .collect(Collectors.joining(" "));
    }

    private Set<String> parseScope(String scopeClaim) {
        if (scopeClaim == null || scopeClaim.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(scopeClaim.split("\\s+"))
                .filter(scope -> scope != null && !scope.isBlank())
                .collect(Collectors.toUnmodifiableSet());
    }

    @SuppressWarnings("unchecked")
    private List<String> toStringList(List<?> values) {
        if (values == null || values.isEmpty()) {
            return List.of();
        }
        return values.stream()
                .filter(item -> item != null && !item.toString().isBlank())
                .map(Object::toString)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
