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

