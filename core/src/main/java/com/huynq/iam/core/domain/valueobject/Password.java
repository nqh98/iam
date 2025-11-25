package com.huynq.iam.core.domain.valueobject;

import java.util.Objects;

/**
 * Value object representing a password in the system.
 */
public class Password {
    private final String hashedValue;

    private Password(String hashedValue) {
        this.hashedValue = hashedValue;
    }

    /**
     * Creates a Password from a plain text password.
     * The password will be hashed before storage.
     *
     * @param plainPassword the plain text password
     * @return the Password instance with hashed value
     * @throws IllegalArgumentException if password is null or empty
     */
    public static Password of(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return new Password(hash(plainPassword));
    }

    /**
     * Creates a Password from an already hashed password value.
     * Used when loading from database or external source.
     *
     * @param hashedPassword the already hashed password
     * @return the Password instance
     * @throws IllegalArgumentException if hashed password is null or empty
     */
    public static Password fromHashed(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        return new Password(hashedPassword);
    }

    /**
     * Checks if a plain password matches this hashed password.
     *
     * @param plainPassword the plain text password to check
     * @return true if the password matches
     */
    public boolean matches(String plainPassword) {
        return hash(plainPassword).equals(this.hashedValue);
    }

    public String getHashedValue() {
        return hashedValue;
    }

    private static String hash(String password) {
        // In a real implementation, you would use a proper password hashing algorithm like BCrypt
        // For now, this is a simple placeholder
        return String.valueOf(password.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Password password = (Password) obj;
        return Objects.equals(hashedValue, password.hashedValue);
    }

    @Override
    public int hashCode() {
        return hashedValue != null ? hashedValue.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "[PROTECTED]";
    }
}
