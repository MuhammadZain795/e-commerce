CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

CREATE TABLE products (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          price NUMERIC(10,2) NOT NULL,
                          quantity INT NOT NULL,
                          deleted BOOLEAN DEFAULT FALSE,
                          created_at TIMESTAMP,
                          updated_at TIMESTAMP
);

CREATE TABLE orders (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT NOT NULL,
                        order_total NUMERIC(10,2),
                        CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE order_item (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            order_id BIGINT NOT NULL,
                            product_id BIGINT NOT NULL,
                            quantity INT NOT NULL,
                            unit_price NUMERIC(10,2) NOT NULL,
                            discount_applied NUMERIC(10,2),
                            total_price NUMERIC(10,2),
                            CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id)
);
