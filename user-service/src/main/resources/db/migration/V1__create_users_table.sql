CREATE TABLE users
(
    id          BINARY(16)   NOT NULL,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    is_admin    BOOLEAN      NOT NULL DEFAULT FALSE, -- Admin flag

    CONSTRAINT `PRIMARY` PRIMARY KEY (id),
    CONSTRAINT `uc_email` UNIQUE (email) -- Ensures email is unique
);

INSERT INTO users (id, name, email, password, is_admin)
VALUES (
           UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'),
           'Project Administrator',
           'admin@simpleproject.com',
           -- This is a sample bcrypt hash for 'password123'.
           -- The application will use a library (like bcrypt) to generate this hash
           -- based on the user's input before running this query.
           '$2y$10$eE0k9vB4Z1sXyC0g7lR6u./4n0.3a2T5yV7q1r8s9t0u1v2w3x4y5z6a7b',
           TRUE
       );