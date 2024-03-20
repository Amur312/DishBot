CREATE TABLE categories
(
    id        BIGINT PRIMARY KEY,
    name      VARCHAR(255),
    parent_id BIGINT,
    FOREIGN KEY (parent_id) REFERENCES categories (id)
);

CREATE TABLE clients
(
    id             BIGINT PRIMARY KEY,
    chat_id        BIGINT,
    first_name     VARCHAR(255),
    last_name      VARCHAR(255),
    phone_number   VARCHAR(255),
    address_client VARCHAR(255),
    tg_name        VARCHAR(255),
    state          VARCHAR(255)
);

CREATE TABLE orders
(
    id           BIGINT PRIMARY KEY,
    created_date TIMESTAMP(6),
    status       VARCHAR(255),
    amount       NUMERIC(38, 2),
    client_id    BIGINT,
    FOREIGN KEY (client_id) REFERENCES clients (id)
);

CREATE TABLE products
(
    id          BIGINT PRIMARY KEY,
    name        VARCHAR(255),
    description VARCHAR(255),
    price       NUMERIC(38, 2),
    photo_url   OID,
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE order_items
(
    id            INTEGER PRIMARY KEY,
    order_id      BIGINT,
    product_id    BIGINT,
    product_price NUMERIC(38, 2),
    product_name  VARCHAR(255),
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);
