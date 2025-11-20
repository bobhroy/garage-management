CREATE TABLE orders
(
    id           BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) NOT NULL,
    customer_id  BINARY(16)                               NOT NULL,
    status       CHAR(15)   DEFAULT ('CREATED')           NOT NULL,
    date_created TIMESTAMP  DEFAULT CURRENT_TIMESTAMP     NOT NULL,
    date_updated TIMESTAMP                                NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id),
    CONSTRAINT orders_customers_id_fk
        FOREIGN KEY (customer_id) REFERENCES customers (id)
);