CREATE USER IF NOT EXISTS 'midnightdineruser'@'%'
    IDENTIFIED BY 'ThisIsAUserAcount:8080';

CREATE DATABASE IF NOT EXISTS midnightdiner
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON midnightdiner.* TO 'midnightdineruser'@'%';

FLUSH PRIVILEGES;

USE midnightdiner;

CREATE TABLE IF NOT EXISTS restaurant_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_name VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    location VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    current_order_id BIGINT,
    grid_x INT NOT NULL,
    grid_y INT NOT NULL,
    grid_width INT NOT NULL DEFAULT 1,
    grid_height INT NOT NULL DEFAULT 1
);

GRANT SELECT, INSERT, UPDATE, DELETE
    ON midnightdiner.restaurant_table TO 'midnightdineruser'@'%';

CREATE TABLE IF NOT EXISTS reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    reservation_time DATETIME NOT NULL,
    number_of_guests INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    table_id BIGINT,
    FOREIGN KEY (table_id) REFERENCES restaurant_table(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE
    ON midnightdiner.reservation TO 'midnightdineruser'@'%';

CREATE TABLE IF NOT EXISTS dish (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description TEXT,
    ingredients TEXT,
    image_url VARCHAR(255),
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE
    ON midnightdiner.dish TO 'midnightdineruser'@'%';

CREATE TABLE IF NOT EXISTS `order` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) DEFAULT 0.00,
    status ENUM('OPEN', 'CLOSED') NOT NULL DEFAULT 'OPEN',
    start_time DATETIME NOT NULL,
    close_time DATETIME,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (table_id) REFERENCES restaurant_table(id) ON DELETE CASCADE
);
GRANT SELECT, INSERT, UPDATE, DELETE
    ON midnightdiner.`order` TO 'midnightdineruser'@'%';

CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    dish_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `order`(id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dish(id) ON DELETE CASCADE
);
GRANT SELECT, INSERT, UPDATE, DELETE
    ON midnightdiner.order_item TO 'midnightdineruser'@'%';

CREATE INDEX idx_order_table_status ON `order` (table_id, status);
CREATE INDEX idx_order_status ON `order` (status);
CREATE INDEX idx_order_item_order_id ON order_item (order_id);
CREATE INDEX idx_order_item_dish_id ON order_item (dish_id);

CREATE TABLE IF NOT EXISTS daily_sales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL UNIQUE,
    total_sales DECIMAL(15,2) NOT NULL,
    create_time DATETIME NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE
    ON midnightdiner.daily_sales TO 'midnightdineruser'@'%';

CREATE TABLE IF NOT EXISTS admin_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

-- By default, insert a super administrator "admin" with the initial password: "ThisIsAdminUser."
-- The password is hashed using the BCrypt algorithm, with the hash value: "$2a$12$hmPTccIwjAcRBNGJjVO8Nu
INSERT INTO admin_user (username, password, role, create_time, update_time)
VALUES ('admin', '$2a$12$hmPTccIwjAcRBNGJjVO8NuNpcR4nOuAb7v8vKArdZW7suf9WcZql.', 'SUPER_ADMIN', NOW(), NOW());

GRANT SELECT
    ON midnightdiner.admin_user TO 'midnightdineruser'@'%';

ALTER TABLE restaurant_table
    ADD CONSTRAINT fk_current_order
        FOREIGN KEY (current_order_id) REFERENCES `order`(id)
            ON UPDATE CASCADE
            ON DELETE SET NULL;

FLUSH PRIVILEGES;
