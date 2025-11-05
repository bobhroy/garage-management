CREATE TABLE customers
(
    id             BINARY(16)                             NOT NULL,
    name           VARCHAR(255)                           NOT NULL,
    phone          VARCHAR(20)                            NOT NULL,
    email          VARCHAR(100)                           NOT NULL,
    loyalty_points INT UNSIGNED DEFAULT '0'               NOT NULL,
    created_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by     VARCHAR(255)                           NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

INSERT INTO customers (id, name, phone, email, loyalty_points, created_by) VALUES
   (UNHEX('A1'), 'Alice Johnson', '555-0101', 'alice.j@mail.com', 150, 'system_admin'),
   (UNHEX('A2'), 'Bob Smith', '555-0102', 'bob.s@mail.com', 25, 'system_admin'),
   (UNHEX('A3'), 'Charlie Brown', '555-0103', 'charlie.b@mail.com', 0, 'user_app'),
   (UNHEX('A4'), 'Dana White', '555-0104', 'dana.w@mail.com', 500, 'system_admin'),
   (UNHEX('A5'), 'Evan Clark', '555-0105', 'evan.c@mail.com', 10, 'user_app');