package com.huynq.iam.core.domain.enums;

public enum ErrorCode {
    USER_NOT_FOUND(-1001, "User not found"),
    ROLE_NOT_FOUND(-1002, "Role not found"),
    EXTERNAL_ID_EXISTS(-1003, "User with external id already exists"),
    INVALID_OLD_PASSWORD(-1004, "Old password does not match current password"),
    INVALID_CREDENTIALS(-1005, "Invalid credentials");

    private final int code;
    private final String defaultMessage;

    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int getCode() { return code; }
    public String getDefaultMessage() { return defaultMessage; }
}
