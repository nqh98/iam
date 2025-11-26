package com.huynq.application.usecase;

import com.huynq.application.dto.CreateUserRequest;
import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.exception.BusinessException;
import com.huynq.iam.core.domain.service.IdentityService;
import com.huynq.iam.core.domain.service.PasswordService;
import com.huynq.iam.core.domain.valueobject.Password;

public class UserUseCase {
    private final IdentityService identityService;
    private final PasswordService passwordService;

    public UserUseCase(IdentityService identityService, PasswordService passwordService) {
        this.identityService = identityService;
        this.passwordService = passwordService;
    }

    /**
     * Creates a new user.
     *
     * @param request the user creation request
     * @return the created user
     */
    public UserEntity createUser(CreateUserRequest request) throws BusinessException {
        // Hash the password using BCrypt before creating the Password value object
        String hashedPassword = passwordService.encode(request.getPassword());
        Password password = Password.fromHashed(hashedPassword);

        return identityService.createUser(password, request.getExternalId());
    }

    /**
     * Assigns a role to a user.
     *
     * @param userId the user ID
     * @param roleId the role ID
     * @return the updated user
     */
    public UserEntity assignRole(Long userId, Long roleId) throws BusinessException {
        return identityService.assignRole(userId, roleId);
    }

    /**
     * Removes a role from a user.
     *
     * @param userId the user ID
     * @param roleId the role ID
     * @return the updated user
     */
    public UserEntity removeRole(Long userId, Long roleId) throws BusinessException {
        return identityService.removeRole(userId, roleId);
    }

    /**
     * Changes a user's password.
     *
     * @param userId the user ID
     * @param oldPassword the old password
     * @param newPassword the new password
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) throws BusinessException {
        Password oldPwd = Password.of(oldPassword);
        Password newPwd = Password.of(newPassword);

        identityService.changePassword(userId, oldPwd, newPwd);
    }
}
