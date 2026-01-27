CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

CREATE TABLE products (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          price NUMERIC(10,2) NOT NULL,
                          quantity INT NOT NULL,
                          deleted BOOLEAN DEFAULT FALSE,
                          created_at TIMESTAMP,
                          updated_at TIMESTAMP
);

CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,
                        user_id BIGINT REFERENCES users(id),
                        order_total NUMERIC(10,2)
);

CREATE TABLE order_item (
                            id SERIAL PRIMARY KEY,
                            order_id BIGINT REFERENCES orders(id),
                            product_id BIGINT,
                            quantity INT,
                            unit_price NUMERIC(10,2),
                            discount_applied NUMERIC(10,2),
                            total_price NUMERIC(10,2)
);
