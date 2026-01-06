INSERT INTO org (
    name,
    domain,
    contact_email,
    is_active,
    created_at,
    updated_at
)
VALUES (
    'Mercer Mettl',
    'mercer.com',
    'admin@mercer.com',
    true,
    NOW(),
    NOW()
);

INSERT INTO roles (role_name, created_at, updated_at)
VALUES
('ADMIN',       NOW(), NOW()),
('ORG_ADMIN',   NOW(), NOW()),
('RECRUITER',   NOW(), NOW()),
('PROCTOR',     NOW(), NOW()),
('CANDIDATE',   NOW(), NOW());

ALTER TABLE users DROP COLUMN status;

ALTER TABLE users ADD COLUMN status VARCHAR(255);


INSERT INTO users (
    org_id,
    email,
    full_name,
    password,
    role_id,
    status,
    is_email_verified,
    is_active,
    created_at,
    updated_at
)
VALUES (
    null,                                  -- org_id
    'admin@gmail.com',                -- email
    'Admin',                     -- full_name
    '$2a$10$2YY5/I7uk8sk.6.PbcQUu.6FXRNKlx.pqlTN6T3tCmgGuM7Sk6JCq',        -- password (bcrypt / hashed)
    1,                                  -- role_id (ADMIN)
    'ACTIVE',                           -- status
    true,                               -- is_email_verified
    true,                               -- is_active
    NOW(),                              -- created_at
    NOW()                               -- updated_at
);


ALTER TABLE user_sessions
ALTER COLUMN refresh_token TYPE TEXT;
