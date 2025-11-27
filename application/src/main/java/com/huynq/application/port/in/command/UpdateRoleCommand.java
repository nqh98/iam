package com.huynq.application.port.in.command;

import java.util.List;
import java.util.Objects;

/**
 * Command describing mutable role fields.
 */
public record UpdateRoleCommand(
        Long roleId,
        String name,
        String description,
        Boolean active,
        List<String> permissions
) {

    public UpdateRoleCommand {
        Objects.requireNonNull(roleId, "roleId is required");
    }

    public static UpdateRoleCommand of(Long roleId,
                                       String name,
                                       String description,
                                       Boolean active,
                                       List<String> permissions) {
        return new UpdateRoleCommand(roleId, name, description, active, permissions);
    }
}

