-- Align Flyway schema with JPA entity mapping.
-- JPA maps OrderItem to table: order_items (see @Table(name="order_items")).
-- V1 created: order_item. In H2 (fresh in-memory) V2 will run after V1.

DROP TABLE IF EXISTS order_items;

CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    discount_applied NUMERIC(10,2),
    total_price NUMERIC(10,2),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- Keep backward-compat for the incorrect table created in V1.
DROP TABLE IF EXISTS order_item;

