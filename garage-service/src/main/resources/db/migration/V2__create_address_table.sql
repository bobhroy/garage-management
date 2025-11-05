CREATE TABLE addresses
(
    id          BINARY(16)   NOT NULL,
    street      VARCHAR(255) NOT NULL,
    city        VARCHAR(255) NOT NULL,
    state       CHAR(36)     NOT NULL,
    zip         CHAR(36)     NOT NULL,
    customer_id BINARY(16)   NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id),
    CONSTRAINT addresses_customers_id_fk
        FOREIGN KEY (customer_id) REFERENCES customers (id)
);

INSERT INTO addresses (id, customer_id, street, city, state, zip) VALUES
    (UNHEX('B1'), UNHEX('A1'), '123 Main St', 'Springfield', 'IL', '62704'),
    (UNHEX('B2'), UNHEX('A2'), '45 Oak Ave', 'Shelbyville', 'KY', '40065'),
    (UNHEX('B3'), UNHEX('A3'), '78 River Rd', 'Capital City', 'CA', '95814'),
    (UNHEX('B4'), UNHEX('A4'), '90 Pine Ln', 'Ogdenville', 'UT', '84401'),
    (UNHEX('B5'), UNHEX('A5'), '11 Elm Blv', 'North Haven', 'CT', '06473');