DROP TABLE IF EXISTS CUSTOMER;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS ASSET;
DROP TABLE IF EXISTS ORDERS;
CREATE TABLE CUSTOMER (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255) UNIQUE,
    email VARCHAR(255),
    try_balance DECIMAL(10, 2)
    );

CREATE TABLE USERS (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          username VARCHAR(255) UNIQUE,
                          password VARCHAR(255),
                          user_role VARCHAR(10) -- ADMIN or CUSTOMER
);

CREATE TABLE  ASSET (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     customer_id BIGINT,
                                     asset_name VARCHAR(255),
    size DECIMAL(10, 2),
    usable_size DECIMAL(10, 2),
    FOREIGN KEY (customer_id) REFERENCES customer(id)
    );

CREATE TABLE  ORDERS (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             customer_id BIGINT,
                             asset_name VARCHAR(255),
                             order_side VARCHAR(10), -- 'BUY' or 'SELL'
                             size DECIMAL(10, 2),
                             price DECIMAL(10, 2),
                             status VARCHAR(10), -- 'PENDING', 'MATCHED', 'CANCELED'
                             create_date DATE,
                             FOREIGN KEY (customer_id) REFERENCES customer(id)
);