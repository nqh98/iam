package com.huynq.application.port.in.command;

import java.util.Objects;

/**
 * Command describing user mutable attributes.
 */
public record UpdateUserCommand(Long userId, String externalId) {

    public UpdateUserCommand {
        Objects.requireNonNull(userId, "userId is required");
        Objects.requireNonNull(externalId, "externalId is required");
    }

    public static UpdateUserCommand of(Long userId, String externalId) {
        return new UpdateUserCommand(userId, externalId);
    }
}

