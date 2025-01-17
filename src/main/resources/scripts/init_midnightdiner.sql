-- =========================
-- 创建用户及数据库
-- =========================

-- 创建新用户 'midnightdineruser'，指定密码
CREATE USER IF NOT EXISTS 'midnightdineruser'@'%'
    IDENTIFIED BY 'ThisIsAUserAcount:8080';

-- 创建数据库 midnightdiner
CREATE DATABASE IF NOT EXISTS midnightdiner
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 授予权限
GRANT ALL PRIVILEGES ON midnightdiner.* TO 'midnightdineruser'@'%';
FLUSH PRIVILEGES;

-- 使用数据库
USE midnightdiner;

-- =========================
-- 创建桌位表 restaurant_table
-- =========================
CREATE TABLE IF NOT EXISTS restaurant_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_name VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    location VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

-- =========================
-- 创建预订表 reservation（带外键）
-- =========================
CREATE TABLE IF NOT EXISTS reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    reservation_time DATETIME NOT NULL,
    number_of_guests INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,

    -- 新增外键字段：table_id
    table_id BIGINT,

    -- 声明外键约束，关联到 restaurant_table 的 id
    CONSTRAINT fk_table_id
        FOREIGN KEY (table_id)
            REFERENCES restaurant_table(id)
            ON UPDATE CASCADE
            ON DELETE SET NULL
);

-- =========================
-- 刷新权限
-- =========================
FLUSH PRIVILEGES;
