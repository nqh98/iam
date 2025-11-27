package com.huynq.application.port.in.command;

import java.util.Objects;

/**
 * Command representing logout/revocation of a refresh token session.
 */
public record RevokeRefreshTokenCommand(
        String refreshToken,
        String reason
) {

    public RevokeRefreshTokenCommand {
        Objects.requireNonNull(refreshToken, "refreshToken is required");
    }

    public static RevokeRefreshTokenCommand of(String refreshToken, String reason) {
        return new RevokeRefreshTokenCommand(refreshToken, reason);
    }
}

