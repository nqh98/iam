package com.huynq.iam.infrastructure.config;

import com.huynq.application.port.in.LoginUseCase;
import com.huynq.application.port.in.RoleCommandUseCase;
import com.huynq.application.port.in.RoleQueryUseCase;
import com.huynq.application.port.in.TokenRefreshUseCase;
import com.huynq.application.port.in.UserAdministrationUseCase;
import com.huynq.application.port.in.UserQueryUseCase;
import com.huynq.application.usecase.AuthenticationUseCase;
import com.huynq.application.usecase.RoleUseCase;
import com.huynq.application.usecase.UserQueryService;
import com.huynq.application.usecase.UserUseCase;
import com.huynq.iam.core.domain.repository.PermissionRepository;
import com.huynq.iam.core.domain.repository.RefreshTokenRepository;
import com.huynq.iam.core.domain.repository.RolePermissionRepository;
import com.huynq.iam.core.domain.repository.RoleRepository;
import com.huynq.iam.core.domain.repository.UserPermissionRepository;
import com.huynq.iam.core.domain.repository.UserRepository;
import com.huynq.iam.core.domain.repository.UserRoleRepository;
import com.huynq.iam.core.domain.service.AuthenticationService;
import com.huynq.iam.core.domain.service.IdGenerator;
import com.huynq.iam.core.domain.service.IdentityService;
import com.huynq.iam.core.domain.service.JwtTokenService;
import com.huynq.iam.core.domain.service.PasswordService;
import com.huynq.iam.core.domain.service.PermissionResolver;
import com.huynq.iam.infrastructure.security.JwtProperties;
import com.huynq.iam.infrastructure.service.AuthenticationServiceImpl;
import com.huynq.iam.infrastructure.service.IdentityServiceImpl;
import com.huynq.iam.infrastructure.service.JwtTokenServiceImpl;
import com.huynq.iam.infrastructure.service.PasswordServiceImpl;
import com.huynq.iam.infrastructure.support.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class IamApplicationConfig {

    @Bean
    public IdGenerator idGenerator(@Value("${app.node-id:1}") long nodeId) {
        return new SnowflakeIdGenerator(nodeId);
    }

    @Bean
    public JwtTokenService jwtTokenService(JwtProperties jwtProperties, SecretKey jwtSecretKey) {
        return new JwtTokenServiceImpl(jwtProperties, jwtSecretKey);
    }

    @Bean
    public PasswordService passwordService(org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        return new PasswordServiceImpl(passwordEncoder);
    }

    @Bean
    public AuthenticationService authenticationService(UserRepository userRepository,
                                                       PasswordService passwordService) {
        return new AuthenticationServiceImpl(userRepository, passwordService);
    }

    @Bean
    public IdentityService identityService(UserRepository userRepository,
                                           RoleRepository roleRepository,
                                           IdGenerator idGenerator,
                                           PasswordService passwordService) {
        return new IdentityServiceImpl(userRepository, roleRepository, idGenerator, passwordService);
    }

    @Bean
    public PermissionResolver permissionResolver(UserRoleRepository userRoleRepository,
                                                 RolePermissionRepository rolePermissionRepository,
                                                 UserPermissionRepository userPermissionRepository,
                                                 PermissionRepository permissionRepository) {
        return new PermissionResolver(
                userRoleRepository,
                rolePermissionRepository,
                userPermissionRepository,
                permissionRepository
        );
    }

    @Bean
    public AuthenticationUseCase authenticationUseCase(AuthenticationService authenticationService,
                                                       UserRepository userRepository,
                                                       UserRoleRepository userRoleRepository,
                                                       RoleRepository roleRepository,
                                                       PermissionResolver permissionResolver,
                                                       JwtTokenService jwtTokenService,
                                                       RefreshTokenRepository refreshTokenRepository) {
        return new AuthenticationUseCase(
                authenticationService,
                userRepository,
                userRoleRepository,
                roleRepository,
                permissionResolver,
                jwtTokenService,
                refreshTokenRepository
        );
    }

    @Bean
    public LoginUseCase loginUseCase(AuthenticationUseCase authenticationUseCase) {
        return authenticationUseCase;
    }

    @Bean
    public TokenRefreshUseCase tokenRefreshUseCase(AuthenticationUseCase authenticationUseCase) {
        return authenticationUseCase;
    }

    @Bean
    public UserUseCase userUseCase(IdentityService identityService) {
        return new UserUseCase(identityService);
    }

    @Bean
    public UserAdministrationUseCase userAdministrationUseCase(UserUseCase userUseCase) {
        return userUseCase;
    }

    @Bean
    public UserQueryService userQueryService(UserRepository userRepository,
                                             RoleRepository roleRepository,
                                             PermissionResolver permissionResolver) {
        return new UserQueryService(userRepository, roleRepository, permissionResolver);
    }

    @Bean
    public UserQueryUseCase userQueryUseCase(UserQueryService userQueryService) {
        return userQueryService;
    }

    @Bean
    public RoleUseCase roleUseCase(RoleRepository roleRepository,
                                   PermissionRepository permissionRepository) {
        return new RoleUseCase(roleRepository, permissionRepository);
    }

    @Bean
    public RoleCommandUseCase roleCommandUseCase(RoleUseCase roleUseCase) {
        return roleUseCase;
    }

    @Bean
    public RoleQueryUseCase roleQueryUseCase(RoleUseCase roleUseCase) {
        return roleUseCase;
    }
}

