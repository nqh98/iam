package com.huynq.application.port.in.command;

import java.util.Objects;

/**
 * Command for assigning a role to a user.
 */
public record AssignRoleCommand(Long userId, Long roleId) {

    public AssignRoleCommand {
        Objects.requireNonNull(userId, "userId is required");
        Objects.requireNonNull(roleId, "roleId is required");
    }

    public static AssignRoleCommand of(Long userId, Long roleId) {
        return new AssignRoleCommand(userId, roleId);
    }
}

