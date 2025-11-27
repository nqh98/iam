package com.huynq.iam.infrastructure.api;

import com.huynq.application.port.in.RoleCommandUseCase;
import com.huynq.application.port.in.RoleQueryUseCase;
import com.huynq.application.port.in.command.CreateRoleCommand;
import com.huynq.application.port.in.command.UpdateRoleCommand;
import com.huynq.application.port.in.result.RoleDetailsResult;
import com.huynq.iam.core.domain.exception.BusinessException;
import com.huynq.iam.infrastructure.api.dto.CreateRoleRequest;
import com.huynq.iam.infrastructure.api.dto.RoleResponse;
import com.huynq.iam.infrastructure.api.dto.UpdateRoleRequest;
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

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleCommandUseCase roleCommandUseCase;
    private final RoleQueryUseCase roleQueryUseCase;

    public RoleController(RoleCommandUseCase roleCommandUseCase,
                          RoleQueryUseCase roleQueryUseCase) {
        this.roleCommandUseCase = roleCommandUseCase;
        this.roleQueryUseCase = roleQueryUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_iam:role:write')")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest request) throws BusinessException {
        RoleDetailsResult result = roleCommandUseCase.createRole(CreateRoleCommand.of(
                request.name(),
                request.description(),
                request.permissions()
        ));
        return ResponseEntity.ok(toResponse(result));
    }

    @PutMapping("/{roleId}")
    @PreAuthorize("hasAuthority('SCOPE_iam:role:write')")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable Long roleId,
                                                   @Valid @RequestBody UpdateRoleRequest request) throws BusinessException {
        RoleDetailsResult result = roleCommandUseCase.updateRole(UpdateRoleCommand.of(
                roleId,
                request.name(),
                request.description(),
                request.active(),
                request.permissions()
        ));
        return ResponseEntity.ok(toResponse(result));
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("hasAuthority('SCOPE_iam:role:read')")
    public ResponseEntity<RoleResponse> getRole(@PathVariable Long roleId) throws BusinessException {
        return ResponseEntity.ok(toResponse(roleQueryUseCase.getRole(roleId)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_iam:role:read')")
    public ResponseEntity<List<RoleResponse>> listRoles() {
        return ResponseEntity.ok(roleQueryUseCase.listRoles().stream()
                .map(this::toResponse)
                .toList());
    }

    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAuthority('SCOPE_iam:role:write')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) throws BusinessException {
        roleCommandUseCase.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    private RoleResponse toResponse(RoleDetailsResult result) {
        return new RoleResponse(
                result.id(),
                result.name(),
                result.description(),
                result.active(),
                result.permissions(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}

