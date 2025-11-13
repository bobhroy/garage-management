CREATE TABLE carts
(
    id             BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) NOT NULL,
    date_created   TIMESTAMP  DEFAULT CURRENT_TIMESTAMP     NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE cart_items
(
    id              BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) NOT NULL
        PRIMARY KEY,
    cart_id         BINARY(16)                               NOT NULL,
    service_type_id BINARY(16)                               NOT NULL,
    technician      VARCHAR(255)                             NULL,
    status          CHAR(15)   DEFAULT ('OPEN')              NOT NULL,
    date_completed  TIMESTAMP                                NULL,
    CONSTRAINT cart_items_cart_service_type_unique
        UNIQUE (cart_id, service_type_id),
    CONSTRAINT cart_items_cart_id_fk
        FOREIGN KEY (cart_id) REFERENCES carts (id)
            ON DELETE CASCADE,
    CONSTRAINT cart_items_service_types_id_fk
        FOREIGN KEY (service_type_id) REFERENCES service_types (id)
            ON DELETE CASCADE
);