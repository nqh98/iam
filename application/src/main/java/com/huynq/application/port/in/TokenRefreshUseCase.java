package com.huynq.application.port.in;

import com.huynq.application.port.in.command.RefreshTokenCommand;
import com.huynq.application.port.in.command.RevokeRefreshTokenCommand;
import com.huynq.application.port.in.result.LoginResult;
import com.huynq.iam.core.domain.exception.BusinessException;

public interface TokenRefreshUseCase {
    LoginResult refresh(RefreshTokenCommand command) throws BusinessException;

    void revoke(RevokeRefreshTokenCommand command) throws BusinessException;
}

