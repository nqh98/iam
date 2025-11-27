package com.huynq.iam.infrastructure.api.dto;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        int code,
        String error,
        String message,
        String path
) {
}

