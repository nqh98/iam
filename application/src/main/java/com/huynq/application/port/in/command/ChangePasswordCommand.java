package com.huynq.application.port.in.command;

import java.util.Objects;

/**
 * Command that instructs the system to change a user's password.
 */
public record ChangePasswordCommand(Long userId, String oldPassword, String newPassword) {

    public ChangePasswordCommand {
        Objects.requireNonNull(userId, "userId is required");
        Objects.requireNonNull(oldPassword, "oldPassword is required");
        Objects.requireNonNull(newPassword, "newPassword is required");
    }

    public static ChangePasswordCommand of(Long userId, String oldPassword, String newPassword) {
        return new ChangePasswordCommand(userId, oldPassword, newPassword);
    }
}

