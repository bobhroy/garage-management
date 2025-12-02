CREATE TABLE users
(
    id          BINARY(16)   NOT NULL,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL DEFAULT 'USER', -- Admin flag

    CONSTRAINT `PRIMARY` PRIMARY KEY (id),
    CONSTRAINT `uc_email` UNIQUE (email) -- Ensures email is unique
);

-- This is a sample admin account for testing.
INSERT INTO users (id, name, email, password, role)
VALUES (
           UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'),
           'Project Administrator',
           'admin@simpleproject.com',
           -- This is a sample bcrypt hash for 'password123'.
           -- The application will use a library (like bcrypt) to generate this hash
           -- based on the user's input before running this query.
           '$2a$10$S8j0HKAyseB6cSTpYam8euc9jvfNV3WUb1b4n5huBe7Q0XllsWpMG',
           'ADMIN'
       );