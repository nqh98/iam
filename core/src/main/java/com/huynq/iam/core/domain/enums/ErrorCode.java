package com.huynq.iam.core.domain.enums;

public enum ErrorCode {
    USER_NOT_FOUND(-1001, "User not found"),
    ROLE_NOT_FOUND(-1002, "Role not found"),
    EXTERNAL_ID_EXISTS(-1003, "User with external id already exists"),
    INVALID_OLD_PASSWORD(-1004, "Old password does not match current password"),
    INVALID_CREDENTIALS(-1005, "Invalid credentials"),
    ROLE_NAME_EXISTS(-1006, "Role with the same name already exists"),
    REFRESH_TOKEN_INVALID(-1007, "Refresh token is invalid"),
    REFRESH_TOKEN_EXPIRED(-1008, "Refresh token expired"),
    REFRESH_TOKEN_REVOKED(-1009, "Refresh token was revoked"),
    REFRESH_TOKEN_REUSE_DETECTED(-1010, "Detected refresh token reuse attempt"),
    ROLE_ALREADY_ASSIGNED(-1011, "Role is already assigned to the user"),
    ROLE_NOT_ASSIGNED(-1012, "Role is not assigned to the user");

    private final int code;
    private final String defaultMessage;

    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int getCode() { return code; }
    public String getDefaultMessage() { return defaultMessage; }
}
