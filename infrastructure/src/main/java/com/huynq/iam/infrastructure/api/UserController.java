package com.huynq.iam.infrastructure.api;

import com.huynq.application.port.in.UserAdministrationUseCase;
import com.huynq.application.port.in.UserQueryUseCase;
import com.huynq.application.port.in.command.AssignRoleCommand;
import com.huynq.application.port.in.command.ChangePasswordCommand;
import com.huynq.application.port.in.command.CreateUserCommand;
import com.huynq.application.port.in.command.RemoveRoleCommand;
import com.huynq.application.port.in.command.UpdateUserCommand;
import com.huynq.application.port.in.result.RoleDetailsResult;
import com.huynq.application.port.in.result.UserDetailsResult;
import com.huynq.iam.core.domain.entity.UserEntity;
import com.huynq.iam.core.domain.exception.BusinessException;
import com.huynq.iam.infrastructure.api.dto.AssignRoleRequest;
import com.huynq.iam.infrastructure.api.dto.ChangePasswordRequest;
import com.huynq.iam.infrastructure.api.dto.CreateUserRequest;
import com.huynq.iam.infrastructure.api.dto.RoleResponse;
import com.huynq.iam.infrastructure.api.dto.UpdateUserRequest;
import com.huynq.iam.infrastructure.api.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserAdministrationUseCase userAdministrationUseCase;
    private final UserQueryUseCase userQueryUseCase;

    public UserController(UserAdministrationUseCase userAdministrationUseCase,
                          UserQueryUseCase userQueryUseCase) {
        this.userAdministrationUseCase = userAdministrationUseCase;
        this.userQueryUseCase = userQueryUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_iam:user:write')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) throws BusinessException {
        UserEntity user = userAdministrationUseCase.createUser(CreateUserCommand.of(
                request.password(),
                request.externalId()
        ));
        return ResponseEntity.ok(fetchUser(user.getId()));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_iam:user:write')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId,
                                                   @Valid @RequestBody UpdateUserRequest request) throws BusinessException {
        userAdministrationUseCase.updateUser(UpdateUserCommand.of(userId, request.externalId()));
        return ResponseEntity.ok(fetchUser(userId));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_iam:user:read')")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) throws BusinessException {
        return ResponseEntity.ok(fetchUser(userId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_iam:user:read')")
    public ResponseEntity<List<UserResponse>> listUsers() {
        List<UserDetailsResult> users = userQueryUseCase.listUsers();
        return ResponseEntity.ok(users.stream().map(this::toResponse).toList());
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_iam:user:write')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) throws BusinessException {
        userAdministrationUseCase.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasAuthority('SCOPE_iam:user:write')")
    public ResponseEntity<UserResponse> assignRole(@PathVariable Long userId,
                                                   @Valid @RequestBody AssignRoleRequest request) throws BusinessException {
        userAdministrationUseCase.assignRole(AssignRoleCommand.of(userId, request.roleId()));
        return ResponseEntity.ok(fetchUser(userId));
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('SCOPE_iam:user:write')")
    public ResponseEntity<UserResponse> removeRole(@PathVariable Long userId,
                                                   @PathVariable Long roleId) throws BusinessException {
        userAdministrationUseCase.removeRole(RemoveRoleCommand.of(userId, roleId));
        return ResponseEntity.ok(fetchUser(userId));
    }

    @PostMapping("/{userId}/password")
    @PreAuthorize("hasAuthority('SCOPE_iam:user:write')")
    public ResponseEntity<Void> changePassword(@PathVariable Long userId,
                                               @Valid @RequestBody ChangePasswordRequest request) throws BusinessException {
        userAdministrationUseCase.changePassword(ChangePasswordCommand.of(
                userId,
                request.oldPassword(),
                request.newPassword()
        ));
        return ResponseEntity.noContent().build();
    }

    private UserResponse fetchUser(Long userId) throws BusinessException {
        UserDetailsResult result = userQueryUseCase.getUser(userId);
        return toResponse(result);
    }

    private UserResponse toResponse(UserDetailsResult result) {
        List<RoleResponse> roles = result.roles() == null
                ? List.of()
                : result.roles().stream().map(this::toRoleResponse).toList();
        return new UserResponse(
                result.id(),
                result.externalId(),
                result.createdAt(),
                result.updatedAt(),
                roles,
                result.scopes()
        );
    }

    private RoleResponse toRoleResponse(RoleDetailsResult role) {
        return new RoleResponse(
                role.id(),
                role.name(),
                role.description(),
                role.active(),
                role.permissions(),
                role.createdAt(),
                role.updatedAt()
        );
    }
}

