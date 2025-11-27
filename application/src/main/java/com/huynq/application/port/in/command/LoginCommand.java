package com.huynq.application.port.in.command;

import java.util.Objects;

/**
 * Input model for the Login use case, independent from transport concerns.
 */
public record LoginCommand(
        String externalId,
        String password,
        String clientId,
        String userAgent,
        String ipAddress
) {

    public LoginCommand {
        Objects.requireNonNull(externalId, "externalId is required");
        Objects.requireNonNull(password, "password is required");
    }

    public static LoginCommand of(String externalId,
                                  String password,
                                  String clientId,
                                  String userAgent,
                                  String ipAddress) {
        return new LoginCommand(externalId, password, clientId, userAgent, ipAddress);
    }
}

