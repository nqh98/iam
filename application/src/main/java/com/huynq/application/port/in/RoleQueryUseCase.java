package com.huynq.application.port.in;

import com.huynq.application.port.in.result.RoleDetailsResult;
import com.huynq.iam.core.domain.exception.BusinessException;

import java.util.List;

public interface RoleQueryUseCase {

    RoleDetailsResult getRole(Long roleId) throws BusinessException;

    List<RoleDetailsResult> listRoles();
}

