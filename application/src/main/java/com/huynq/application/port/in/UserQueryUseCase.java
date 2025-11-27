package com.huynq.application.port.in;

import com.huynq.application.port.in.result.UserDetailsResult;
import com.huynq.iam.core.domain.exception.BusinessException;

import java.util.List;

public interface UserQueryUseCase {

    UserDetailsResult getUser(Long userId) throws BusinessException;

    List<UserDetailsResult> listUsers();
}

