package com.huynq.application.port.in.command;

import java.util.List;
import java.util.Objects;

/**
 * Command describing attributes needed to create a role.
 */
public record CreateRoleCommand(
        String name,
        String description,
        List<String> permissions
) {

    public CreateRoleCommand {
        Objects.requireNonNull(name, "name is required");
    }

    public static CreateRoleCommand of(String name, String description, List<String> permissions) {
        return new CreateRoleCommand(name, description, permissions);
    }
}

