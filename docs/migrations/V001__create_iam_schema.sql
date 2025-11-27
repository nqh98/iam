-- ====================================================================
-- V001__create_iam_schema.sql
-- Apply with: psql -h <host> -U <user> -d <db> -f V001__create_iam_schema.sql
-- ====================================================================

BEGIN;

CREATE TABLE IF NOT EXISTS iam_users (
    id           BIGINT PRIMARY KEY,
    external_id  VARCHAR(150) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    created_at   BIGINT NOT NULL,
    updated_at   BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS iam_roles (
    id           BIGINT PRIMARY KEY,
    name         VARCHAR(150) NOT NULL UNIQUE,
    permissions  JSONB NOT NULL DEFAULT '[]'::JSONB,
    created_at   BIGINT NOT NULL,
    updated_at   BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS iam_user_roles (
    user_id BIGINT NOT NULL REFERENCES iam_users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES iam_roles(id) ON DELETE CASCADE,
    assigned_at BIGINT NOT NULL DEFAULT (EXTRACT(EPOCH FROM NOW())::BIGINT * 1000),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS iam_refresh_tokens (
    id          BIGINT PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES iam_users(id) ON DELETE CASCADE,
    token       VARCHAR(255) NOT NULL,
    scopes      JSONB NOT NULL DEFAULT '[]'::JSONB,
    expires_at  BIGINT NOT NULL,
    created_at  BIGINT NOT NULL,
    UNIQUE (token)
);

CREATE INDEX IF NOT EXISTS idx_iam_users_id ON iam_users (id);
CREATE INDEX IF NOT EXISTS idx_iam_roles_id ON iam_roles (id);
CREATE INDEX IF NOT EXISTS idx_iam_user_roles_user ON iam_user_roles (user_id);
CREATE INDEX IF NOT EXISTS idx_iam_user_roles_role ON iam_user_roles (role_id);
CREATE INDEX IF NOT EXISTS idx_iam_refresh_tokens_user ON iam_refresh_tokens (user_id);
CREATE INDEX IF NOT EXISTS idx_iam_refresh_tokens_token ON iam_refresh_tokens (token);

CREATE OR REPLACE VIEW user_with_roles_and_permissions AS
SELECT
    u.id,
    u.external_id,
    u.created_at,
    u.updated_at,
    COALESCE(
        jsonb_agg(
            jsonb_build_object(
                'role_id', r.id,
                'role_name', r.name,
                'permissions', r.permissions
            )
            ORDER BY r.name
        ) FILTER (WHERE r.id IS NOT NULL),
        '[]'::jsonb
    ) AS roles,
    COALESCE(
        (
            SELECT jsonb_agg(DISTINCT perm)
            FROM (
                SELECT jsonb_array_elements(r.permissions) AS perm
                FROM iam_roles r
                JOIN iam_user_roles ur2 ON ur2.role_id = r.id
                WHERE ur2.user_id = u.id
            ) role_perms
        ),
        '[]'::jsonb
    ) AS combined_role_permissions
FROM iam_users u
LEFT JOIN iam_user_roles ur ON ur.user_id = u.id
LEFT JOIN iam_roles r ON r.id = ur.role_id
GROUP BY u.id;

INSERT INTO iam_roles (id, name, permissions, created_at, updated_at) VALUES
    (720000000000000001, 'ADMIN', '[ "iam:user:read", "iam:user:write", "iam:role:write" ]'::jsonb, 1700000000000, 1700000000000)
ON CONFLICT (id) DO NOTHING;

INSERT INTO iam_roles (id, name, permissions, created_at, updated_at) VALUES
    (720000000000000002, 'ANALYST', '[ "iam:user:read", "reports:export" ]'::jsonb, 1700000000000, 1700000000000)
ON CONFLICT (id) DO NOTHING;

INSERT INTO iam_users (id, external_id, password, created_at, updated_at) VALUES
    (710000000000000001, 'alice@example.com', '$2a$12$abcdefghijklmnopqrstuvwxyz0123456789AB', 1700000000000, 1700000000000)
ON CONFLICT (id) DO NOTHING;

INSERT INTO iam_users (id, external_id, password, created_at, updated_at) VALUES
    (710000000000000002, 'bob@example.com', '$2a$12$ZYXWVUTSRQPONMLKJIHGFEDCBA9876543210', 1700000000000, 1700000000000)
ON CONFLICT (id) DO NOTHING;

INSERT INTO iam_user_roles (user_id, role_id, assigned_at) VALUES
    (710000000000000001, 720000000000000001, 1700000005000)
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO iam_user_roles (user_id, role_id, assigned_at) VALUES
    (710000000000000002, 720000000000000002, 1700000005000)
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO iam_refresh_tokens (id, user_id, token, scopes, expires_at, created_at) VALUES
    (730000000000000001, 710000000000000001, 'refresh-token-alice', '[ "iam:user:read", "iam:user:write", "iam:role:write" ]'::jsonb, 1700600000000, 1700000000000)
ON CONFLICT (id) DO NOTHING;

INSERT INTO iam_refresh_tokens (id, user_id, token, scopes, expires_at, created_at) VALUES
    (730000000000000002, 710000000000000002, 'refresh-token-bob', '[ "iam:user:read", "reports:export" ]'::jsonb, 1700600000000, 1700000000000)
ON CONFLICT (id) DO NOTHING;

COMMIT;

