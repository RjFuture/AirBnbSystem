# AirBnB System

A monolithic backend for an Airbnb-style property booking platform, built with Spring Boot. It covers the core flows of a booking marketplace: hotel and room management for owners, availability tracking through per-room inventory, and secure, role-based access for guests and hotel managers.

## Features

- **Authentication and authorization**
  - JWT access tokens with a refresh token stored in an HTTP cookie
  - Role-based access control (`GUEST`, `HOTEL_MANAGER`)
  - Ownership checks, so a manager can only modify their own hotels and rooms
- **Hotel and room management**
  - Hotel managers create and manage hotels and the rooms inside them
  - Room inventory is initialized for a full year when a hotel is active
- **Bookings**
  - Authenticated users can create and manage bookings against available inventory
- **Centralized error handling**
  - A global exception handler returns consistent JSON error responses
  - Security-layer errors (401 and 403) are routed through the same handler
  - Bean Validation failures return field-level messages in a `subErrors` list

## Tech Stack

| Area | Technology |
|---|---|
| Language | Java |
| Framework | Spring Boot, Spring Web MVC |
| Persistence | Spring Data JPA, Hibernate, MySQL |
| Security | Spring Security, JWT (jjwt) |
| Mapping and utilities | ModelMapper, Lombok |
| Build | Maven |

## Architecture

The application is a layered monolith:

```
Controller  ->  Service  ->  Repository  ->  MySQL
     |
Security filter chain (JWT filter, access denied handler, authentication entry point)
     |
Global exception handler (@RestControllerAdvice)
```

All endpoints are served under the `/api/v1` base path.

## Error Response Format

Every error follows the same structure:

```json
{
  "data": null,
  "error": {
    "status": "BAD_REQUEST",
    "message": "Validation Error",
    "subErrors": ["name: must not be blank"]
  },
  "timeStamp": "2026-10-12T01:06:24.670"
}
```

| Case | Status |
|---|---|
| Missing, invalid or expired token | 401 Unauthorized |
| Wrong role or not the resource owner | 403 Forbidden |
| Resource not found | 404 Not Found |
| Validation failure | 400 Bad Request |
| Unexpected server error | 500 Internal Server Error |

## Getting Started

### Prerequisites

- JDK 21 or later
- Maven 3.9 or later
- MySQL 8

### Setup

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd AirBnbSystem
   ```
2. Create a MySQL database:
   ```sql
   CREATE DATABASE airBnb;
   ```
3. Configure `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/airBnb
   spring.datasource.username=<your-username>
   spring.datasource.password=<your-password>
   spring.jpa.hibernate.ddl-auto=update
   server.servlet.context-path=/api/v1
   ```
   Also set the JWT secret key used for signing tokens, as defined in your configuration.
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
   The API is available at `http://localhost:8080/api/v1`.

## Authentication Flow

1. **Sign up and log in** to receive an access token in the response body and a refresh token in a cookie.
2. Send the access token as `Authorization: Bearer <token>` on protected requests.
3. When the access token expires, call `POST /api/v1/auth/refresh`. The refresh token is read from the cookie and a new access token is returned.

## Project Structure

```
com.onlinebookingsystem.airbnbsystem
├── advice        # Global exception handler and error models
├── controller    # REST controllers
├── dto           # Request and response objects
├── exceptions    # Custom exceptions
├── models        # JPA entities
├── repository    # Spring Data repositories
├── security      # JWT filter and security configuration
└── service       # Business logic
```

## Roadmap

- Payment integration
- Hotel search and filtering improvements
- Automated tests (unit and integration)
- API documentation with OpenAPI/Swagger
- Containerization with Docker
