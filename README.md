# Restaurant Web Backend

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](./LICENSE)

This is the backend project for a restaurant management system, built with **Spring Boot** and paired with a **React + Ant Design (antd)** admin panel. The system provides essential functionalities for restaurant operations, including table management, reservation management, menu management, order management, and revenue tracking.

> **Frontend Repository**: [RestaurantWebAdminFrontend](https://github.com/GatsbyBytheSea/RestaurantWebAdminFrontend)
> **Backend Repository**: [RestaurantWebBackend](https://github.com/GatsbyBytheSea/RestaurantWebBackend)

## Features

- **Table Management**:
  - View all tables, update table status (AVAILABLE/RESERVED/IN_USE, etc.), create, delete, and modify tables.
- **Reservation Management**:
  - Create table reservations with dining time, number of guests, and customer contact details. Support canceling and confirming reservations.
- **Menu Management**:
  - View the list of dishes, create, delete, and modify dishes, and upload dish images.
- **Order Management**:
  - Create orders, add or remove items, and finalize payments (CLOSE orders).
- **Sales Statistics**:
  - View daily revenue and order details, automatically track sales when orders are closed.

## Tech Stack

- **Backend Framework**: [Spring Boot](https://spring.io/projects/spring-boot)
- **Security & Authentication**: [Spring Security](https://spring.io/projects/spring-security)
- **Database**: MySQL
- **Data Access**: Spring Data JPA (Repository)
- **Persistence**: JPA + Hibernate
- **Build Tool**: Maven
- **Others**:
  - BCrypt password encryption
  - RESTful API design
  - [Java 17+](https://docs.oracle.com/en/java/)

## Project Structure

```
RestaurantWebBackend
├─ src
│  ├─ main
│  │  ├─ java
│  │  │  └─ com.sunnyserenade.midnightdiner
│  │  │     ├─ config                   # Spring Security, CORS, static resource configuration
│  │  │     ├─ controller               # API controllers
│  │  │     ├─ dto                      # Data Transfer Objects (DTO)
│  │  │     ├─ entity                   # Entity classes mapping to database tables
│  │  │     ├─ repository               # Data access layer (JPA Repository)
│  │  │     ├─ service                  # Business logic layer
│  │  │     └─ MidnightdinerApplication # Main Spring Boot application class
│  │  ├─ resources
│  │  │  └─ scripts                     # Database initialization scripts
│  │  ├─ application.yml                # Spring Boot configuration
│  │  └─ webapp                         # Static resources (if any)
├─ pom.xml
└─ README.md                            # Project documentation
```

## Quick Start

Ensure you have **JDK**, **Maven**, and **MySQL** installed.

1. **Clone the project**
   ```bash
   git clone https://github.com/GatsbyBytheSea/RestaurantWebBackend.git
   ```

2. **Initialize the database**
   - Locate the database initialization script under [`resources/scripts/`](./src/main/resources/scripts) and execute it in MySQL.
   - The script will create a database `midnightdiner` and grant access to the `midnightdineruser` user. Modify credentials as needed.

3. **Configure database connection**
   - Update `application.yml` or `application.properties` with your MySQL connection details.

4. **Install dependencies & start the application**
   ```bash
   # Navigate to backend project directory
   cd RestaurantWebBackend

   # Build and run the application
   mvn clean package
   mvn spring-boot:run
   ```
   - The application will start on **port 8080** by default.

5. **Access the backend API**
   - Base URL: `http://localhost:8080/`
   - API endpoints:
     - `ReservationController`: Public reservation endpoints.
     - `AdminReservationController`: Admin-only reservation management.
     - `AdminTableController`: Table management.
     - `DishController`: Menu management, including image uploads.
     - `OrderController`: Order management.
     - `DailySalesController`: Sales statistics.

6. **Run with Frontend**
   - The frontend repository: [RestaurantWebAdminFrontend](https://github.com/GatsbyBytheSea/RestaurantWebAdminFrontend)
   - Use `npm install && npm run build / start` to build and run the frontend. The default API endpoint is `http://localhost:8080`.

## Key Configurations

- **CORS**:
  - Configured in [`WebCorsConfig`](./src/main/java/com/sunnyserenade/midnightdiner/config/WebCorsConfig.java) to allow all origins (`*`) for easier development.

- **Spring Security**:
  - Login API: `/api/v1/admin/auth/login`
  - Admin credentials: `admin / ThisIsAdminUser.` (BCrypt encrypted).

- **File Upload**:
  - Dish images are stored in `/var/www/images/dishes/`, accessible via `http://localhost:8080/images/dishes/xxx.jpg`.

## Common Issues

1. **Database connection failure**:
   - Ensure MySQL is running and credentials are correct.
   - Check if the initialization script was executed properly.

2. **Image upload failure**:
   - Ensure `/var/www/images/dishes/` exists with proper write permissions.

3. **Login or API authentication issues (401 Unauthorized)**:
   - Verify username and password.
   - Check logs for `Invalid credentials` errors.

## Version & Roadmap

- **Current Version**: Initial stable release.
- **Future Plans**:
  - Dockerized deployment.
  - Multi-language support.

## Contributing

Contributions are welcome! Feel free to open an Issue or submit a Pull Request.

1. Fork the repository and clone locally.
2. Create a new branch for modifications.
3. Commit changes and open a PR.

## License

This project is licensed under the [MIT License](./LICENSE).
Copyright (c) 2025 [**Xinlei ZHU**](https://github.com/GatsbyBytheSea)

---

> **Copyright &copy; 2025 [Xinlei ZHU](https://github.com/GatsbyBytheSea).**
> Licensed under the MIT License.

