package com.huynq.iam.core.domain.service;

import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.exception.BusinessException;
import com.huynq.iam.core.domain.valueobject.Password;

public interface IdentityService {
    /**
     * Creates a new user with the given details.
     *
     * @param password   the password for the new user
     * @param externalId the external id for the new user
     * @return the created user
     */
    UserEntity createUser(Password password, String externalId) throws BusinessException;

    UserEntity assignRole(long userId, long roleId) throws BusinessException;

    UserEntity removeRole(long userId, long roleId) throws BusinessException;

    void changePassword(long userId, Password oldPwd, Password newPwd) throws BusinessException;
}
