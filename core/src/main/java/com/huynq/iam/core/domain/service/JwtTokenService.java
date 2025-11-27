package com.huynq.iam.core.domain.service;

import com.huynq.iam.core.domain.valueobject.RefreshTokenMetadata;
import com.huynq.iam.core.domain.valueobject.TokenPair;

import java.util.List;
import java.util.Set;

public interface JwtTokenService {
    TokenPair generateTokenPair(long userId,
                                String externalId,
                                List<String> roles,
                                Set<String> scopes);

    RefreshTokenMetadata parseRefreshToken(String refreshToken);
}
