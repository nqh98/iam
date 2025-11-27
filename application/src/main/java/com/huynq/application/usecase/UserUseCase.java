package com.huynq.application.usecase;

import com.huynq.application.port.in.UserAdministrationUseCase;
import com.huynq.application.port.in.command.AssignRoleCommand;
import com.huynq.application.port.in.command.ChangePasswordCommand;
import com.huynq.application.port.in.command.CreateUserCommand;
import com.huynq.application.port.in.command.RemoveRoleCommand;
import com.huynq.application.port.in.command.UpdateUserCommand;
import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.exception.BusinessException;
import com.huynq.iam.core.domain.service.IdentityService;

/**
 * Coordinates user-management commands with the identity domain service.
 */
public class UserUseCase implements UserAdministrationUseCase {
    private final IdentityService identityService;

    public UserUseCase(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Override
    public UserEntity createUser(CreateUserCommand command) throws BusinessException {
        return identityService.createUser(command.password(), command.externalId());
    }

    @Override
    public UserEntity updateUser(UpdateUserCommand command) throws BusinessException {
        return identityService.updateExternalId(command.userId(), command.externalId());
    }

    @Override
    public UserEntity assignRole(AssignRoleCommand command) throws BusinessException {
        return identityService.assignRole(command.userId(), command.roleId());
    }

    @Override
    public UserEntity removeRole(RemoveRoleCommand command) throws BusinessException {
        return identityService.removeRole(command.userId(), command.roleId());
    }

    @Override
    public void changePassword(ChangePasswordCommand command) throws BusinessException {
        identityService.changePassword(command.userId(), command.oldPassword(), command.newPassword());
        // TODO revoke all tokens after password change
    }

    @Override
    public void deleteUser(Long userId) throws BusinessException {
        identityService.deleteUser(userId);
    }
}
