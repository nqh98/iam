package com.huynq.application.port.in;

import com.huynq.application.port.in.command.AssignRoleCommand;
import com.huynq.application.port.in.command.ChangePasswordCommand;
import com.huynq.application.port.in.command.CreateUserCommand;
import com.huynq.application.port.in.command.RemoveRoleCommand;
import com.huynq.application.port.in.command.UpdateUserCommand;
import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.exception.BusinessException;

public interface UserAdministrationUseCase {

    UserEntity createUser(CreateUserCommand command) throws BusinessException;

    UserEntity updateUser(UpdateUserCommand command) throws BusinessException;

    UserEntity assignRole(AssignRoleCommand command) throws BusinessException;

    UserEntity removeRole(RemoveRoleCommand command) throws BusinessException;

    void changePassword(ChangePasswordCommand command) throws BusinessException;

    void deleteUser(Long userId) throws BusinessException;
}

