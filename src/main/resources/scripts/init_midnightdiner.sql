-- 本脚本用于初始化项目所需的数据库和表结构，需要有管理员权限的用户在MySQL中执行

-- 创建MySQL用户“midnightdineruser”，处理与项目相关的数据库
-- 密码可以自行修改，但需保证与项目配置文件中的一致
CREATE USER IF NOT EXISTS 'midnightdineruser'@'%'
    IDENTIFIED BY 'ThisIsAUserAcount:8080';

-- 创建项目数据库“midnightdiner”
CREATE DATABASE IF NOT EXISTS midnightdiner
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 授予MySQL用于数据库权限
GRANT ALL PRIVILEGES ON midnightdiner.* TO 'midnightdineruser'@'%';
FLUSH PRIVILEGES;

-- 使用数据库
USE midnightdiner;


-- 创建桌位表restaurant_table
CREATE TABLE IF NOT EXISTS restaurant_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_name VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    location VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE
    ON midnightdiner.restaurant_table TO 'midnightdineruser'@'%';


-- 创建预订表 reservation
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

-- 创建菜品表 dish
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

-- 订单表
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
    ON midnightdiner.order TO 'midnightdineruser'@'%';

-- 订单明细表
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    dish_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `order`(id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dish(id) ON DELETE CASCADE
);
GRANT SELECT, INSERT, UPDATE, DELETE
    ON midnightdiner.order_item TO 'midnightdineruser'@'%';

-- 索引优化
CREATE INDEX idx_order_table_status ON `order` (table_id, status);
CREATE INDEX idx_order_status ON `order` (status);
CREATE INDEX idx_order_item_order_id ON order_item (order_id);
CREATE INDEX idx_order_item_dish_id ON order_item (dish_id);

-- 每日营业统计表
CREATE TABLE IF NOT EXISTS daily_sales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL UNIQUE,
    total_sales DECIMAL(15,2) NOT NULL,
    create_time DATETIME NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE
    ON midnightdiner.daily_sales TO 'midnightdineruser'@'%';

-- 创建后台管理用户表 admin_user
CREATE TABLE IF NOT EXISTS admin_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

-- 默认插入一个超级管理员"admin"，初始用户密码为："ThisIsAdminUser."
-- 采用BCrypt加密算法，哈希值为："$2a$12$hmPTccIwjAcRBNGJjVO8NuNpcR4nOuAb7v8vKArdZW7suf9WcZql."
INSERT INTO admin_user (username, password, role, create_time, update_time)
VALUES ('admin', '$2a$12$hmPTccIwjAcRBNGJjVO8NuNpcR4nOuAb7v8vKArdZW7suf9WcZql.', 'SUPER_ADMIN', NOW(), NOW());

-- 设置权限
GRANT SELECT
    ON midnightdiner.admin_user TO 'midnightdineruser'@'%';

FLUSH PRIVILEGES;
