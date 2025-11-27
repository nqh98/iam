package com.huynq.application.port.in;

import com.huynq.application.port.in.command.CreateRoleCommand;
import com.huynq.application.port.in.command.UpdateRoleCommand;
import com.huynq.application.port.in.result.RoleDetailsResult;
import com.huynq.iam.core.domain.exception.BusinessException;

public interface RoleCommandUseCase {

    RoleDetailsResult createRole(CreateRoleCommand command) throws BusinessException;

    RoleDetailsResult updateRole(UpdateRoleCommand command) throws BusinessException;

    void deleteRole(Long roleId) throws BusinessException;
}

