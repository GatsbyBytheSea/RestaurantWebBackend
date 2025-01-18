-- ==========================
-- 创建用户及数据库
-- ==========================

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

-- ==========================
-- 创建桌位表 restaurant_table
-- ==========================
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
    ON midnightdiner.restaurant_table
    TO 'midnightdineruser'@'%';

-- ==========================
-- 创建预订表 reservation
-- ==========================
CREATE TABLE IF NOT EXISTS reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    reservation_time DATETIME NOT NULL,
    number_of_guests INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,

    -- 外键字段：table_id，关联到 restaurant_table 的 id
    table_id BIGINT,
    CONSTRAINT fk_table_id
        FOREIGN KEY (table_id)
            REFERENCES restaurant_table(id)
            ON UPDATE CASCADE
            ON DELETE SET NULL
);
-- 设置权限
GRANT SELECT, INSERT, UPDATE, DELETE
    ON midnightdiner.reservation
    TO 'midnightdineruser'@'%';

-- ==========================
-- 创建后台管理用户表 admin_user
-- ==========================
CREATE TABLE IF NOT EXISTS admin_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

-- 插入一个超级管理员admin
-- admin初始用户密码为：ThisIsAdminUser.
-- 如需修改初始密码请将password参数替换为其它值。
-- Spring Security通常使用BCrypt加密，需要将此处密码修改为自定义密码的BCrypt哈希值。
INSERT INTO admin_user (username, password, role, create_time, update_time)
VALUES ('admin', '{bcrypt}$2a$12$hmPTccIwjAcRBNGJjVO8NuNpcR4nOuAb7v8vKArdZW7suf9WcZql.', 'SUPER_ADMIN', NOW(), NOW());


GRANT SELECT
    ON midnightdiner.admin_user
    TO 'midnightdineruser'@'%';

-- ==========================
-- 刷新权限
-- ==========================
FLUSH PRIVILEGES;
