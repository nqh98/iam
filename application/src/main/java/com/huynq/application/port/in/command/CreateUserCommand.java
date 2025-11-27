package com.huynq.application.port.in.command;

import java.util.Objects;

/**
 * Command describing how to create a new user.
 */
public record CreateUserCommand(String password, String externalId) {

    public CreateUserCommand {
        Objects.requireNonNull(password, "password is required");
        Objects.requireNonNull(externalId, "externalId is required");
    }

    public static CreateUserCommand of(String password, String externalId) {
        return new CreateUserCommand(password, externalId);
    }
}

