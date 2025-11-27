package com.huynq.iam.infrastructure.api.dto;

import jakarta.validation.constraints.NotNull;

public record AssignRoleRequest(
        @NotNull Long roleId
) {
}

