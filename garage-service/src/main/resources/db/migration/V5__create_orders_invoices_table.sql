CREATE TABLE orders
(
    id           BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) NOT NULL,
    customer_id  BINARY(16)                               NOT NULL,
    cart_id      BINARY(16)                               NOT NULL,
    status       CHAR(15)   DEFAULT ('CREATED')           NOT NULL,
    date_created TIMESTAMP  DEFAULT CURRENT_TIMESTAMP     NOT NULL,
    date_updated TIMESTAMP                                NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE invoices
(
    id       BINARY(16)     DEFAULT (UUID_TO_BIN(UUID())) NOT NULL,
    order_id BINARY(16)                                   NOT NULL,
    name     VARCHAR(255)                                 NOT NULL,
    price    DECIMAL(10, 2) DEFAULT 0.00                  NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id),
    CONSTRAINT invoices_orders_id_fk
        FOREIGN KEY (order_id) REFERENCES orders (id)
);