package com.huynq.application.port.in.command;

import java.util.Objects;

/**
 * Command for exchanging a refresh token for a new token pair.
 */
public record RefreshTokenCommand(
        String refreshToken,
        String clientId,
        String userAgent,
        String ipAddress
) {

    public RefreshTokenCommand {
        Objects.requireNonNull(refreshToken, "refreshToken is required");
    }

    public static RefreshTokenCommand of(String refreshToken,
                                         String clientId,
                                         String userAgent,
                                         String ipAddress) {
        return new RefreshTokenCommand(refreshToken, clientId, userAgent, ipAddress);
    }
}

