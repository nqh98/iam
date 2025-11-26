package com.huynq.iam.core.domain.service;

import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.exception.BusinessException;

public interface AuthenticationService {
    UserEntity authenticate(long id, String password) throws BusinessException;
}
