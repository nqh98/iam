# Clean Architecture Blueprint

This project follows a classic Clean Architecture layout with clear directional
dependencies:

```
               +--------------------+
               |   Presentation     |  (REST controllers, filters, schedulers)
               +----------▲---------+
                          |
               +----------+---------+
               |     Application    |  (Use cases, input/output models, ports)
               +----------▲---------+
                          |
               +----------+---------+
               |       Domain       |  (Entities, value objects, enums)
               +--------------------+
```

## Modules

| Module        | Role                                                                                     |
|---------------|------------------------------------------------------------------------------------------|
| `core`        | Domain entities (`UserEntity`, `RoleEntity`, ...), immutable value objects, repositories |
| `application` | Use cases (`AuthenticationUseCase`), input commands, result models, and input ports      |
| `infrastructure` | Framework adapters (security filters, token/jwt implementation, Spring wiring)       |

## Key Principles

1. **Dependency Rule** – inner layers must not depend on outer layers. For example,
   `AuthenticationUseCase` (application) depends only on domain (`core`) ports and models.
2. **Framework Agnostic** – Business logic contains no Spring annotations. All framework
   specifics live in infrastructure adapters/configuration.
3. **Model Ownership** – Transport-specific DTOs belong to adapters. Use cases use their own
   transport-agnostic input (`LoginCommand`) and output (`LoginResult`) models.
4. **Port/Adapter** – Interfaces used by the domain/application are declared upstream
   (`core` repositories, `application` use-case ports). Infrastructure implements them.

## Login Flow Example

1. HTTP adapter converts request JSON → `LoginCommand`.
2. `LoginUseCase` authenticates via `AuthenticationService`, resolves roles & permissions.
3. `PermissionResolver` merges role scopes and overrides.
4. `JwtTokenService` issues access/refresh tokens.
5. Use case returns `LoginResult`; adapter maps to HTTP response DTO.

## Adding a New Use Case

1. Define a port interface in `application.port.in`.
2. Create input command/result records in `application.port.in.command/result`.
3. Implement the use case in `application.usecase`.
4. Introduce any additional repositories in `core.domain.repository`.
5. Implement adapters (web, persistence, messaging) in `infrastructure`.

Following this workflow keeps the core model independent and testable while allowing
multiple delivery mechanisms (REST, gRPC, CLI, etc.) to reuse the same business rules.

