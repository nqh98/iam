package com.huynq.application.usecase;

import com.huynq.application.dto.CreateUserRequest;
import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.exception.BusinessException;
import com.huynq.iam.core.domain.service.IdentityService;

public class UserUseCase {
    private final IdentityService identityService;

    public UserUseCase(IdentityService identityService) {
        this.identityService = identityService;
    }

    /**
     * Creates a new user.
     *
     * @param request the user creation request
     * @return the created user
     */
    public UserEntity createUser(CreateUserRequest request) throws BusinessException {
        return identityService.createUser(request.getPassword(), request.getExternalId());
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
        identityService.changePassword(userId, oldPassword, newPassword);
        //TODO revoke all token
    }
}
