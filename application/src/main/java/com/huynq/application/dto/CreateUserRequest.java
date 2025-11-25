package com.huynq.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a new user.
 */
public class CreateUserRequest {
    @NotBlank(message = "Password cannot be empty")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{}|;:',.<>/?]).{8,}$",
            message = "Password must be at least 8 characters, include uppercase, lowercase, number, and special character"
    )
    private String password;

    @NotBlank(message = "ExternalId cannot be blank")
    @Size(max = 50, message = "ExternalId must be at most 50 characters")
    private String externalId;

    // Default constructor
    public CreateUserRequest() {
    }

    // Constructor with all fields
    public CreateUserRequest(String password, String externalId) {
        this.password = password;
        this.externalId = externalId;
    }

    // Getters and setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
