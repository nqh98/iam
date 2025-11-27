package com.huynq.application.port.in;

import com.huynq.application.port.in.command.LoginCommand;
import com.huynq.application.port.in.result.LoginResult;
import com.huynq.iam.core.domain.exception.BusinessException;

public interface LoginUseCase {

    LoginResult login(LoginCommand command) throws BusinessException;
}

