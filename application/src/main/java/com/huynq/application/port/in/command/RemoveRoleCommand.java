package com.huynq.application.port.in.command;

import java.util.Objects;

/**
 * Command for removing a role from a user.
 */
public record RemoveRoleCommand(Long userId, Long roleId) {

    public RemoveRoleCommand {
        Objects.requireNonNull(userId, "userId is required");
        Objects.requireNonNull(roleId, "roleId is required");
    }

    public static RemoveRoleCommand of(Long userId, Long roleId) {
        return new RemoveRoleCommand(userId, roleId);
    }
}

