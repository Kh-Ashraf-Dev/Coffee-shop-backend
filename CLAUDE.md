# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Coffee Shop Backend is a Spring Boot REST API for a mobile coffee shop application. It provides endpoints for user authentication, product management, order processing, and user management.

**Technology Stack:**
- Spring Boot 3.5.7
- Java 17
- PostgreSQL with Flyway migrations
- JWT authentication
- MapStruct for DTO mapping
- Lombok for reducing boilerplate
- OpenAPI/Swagger for API documentation

## Common Commands

### Database Setup
```bash
# Start PostgreSQL and pgAdmin using Docker Compose
docker-compose up -d

# Stop services
docker-compose down

# pgAdmin is available at: http://localhost:5050
# - Email: admin@coffeeshop.com
# - Password: admin
```

### Build and Run
```bash
# Build the project (Windows)
mvnw.cmd clean install

# Build the project (Unix/Linux)
./mvnw clean install

# Run the application (Windows)
mvnw.cmd spring-boot:run

# Run the application (Unix/Linux)
./mvnw spring-boot:run

# Skip tests during build
mvnw.cmd clean install -DskipTests
```

### Testing
```bash
# Run all tests
mvnw.cmd test

# Run tests for a specific class
mvnw.cmd test -Dtest=CoffeeShopBackendApplicationTests

# Run tests with coverage
mvnw.cmd test jacoco:report
```

### Development
```bash
# Clean build artifacts
mvnw.cmd clean

# Compile without running tests
mvnw.cmd compile

# Package the application
mvnw.cmd package
```

## Architecture

### Package Structure
```
com.coffeeshop/
├── config/           # Configuration classes (Security, OpenAPI)
├── controller/       # REST controllers
├── dto/             # Data Transfer Objects (request/response)
├── entity/          # JPA entities
├── enums/           # Enumerations (OrderStatus, UserRole, etc.)
├── exception/       # Custom exceptions and global handler
├── repository/      # Spring Data JPA repositories
├── security/        # Security components (JWT, filters)
└── service/         # Business logic layer
```

### Key Architectural Patterns

**Entity-Service-Controller Pattern:**
- Entities extend `BaseEntity` which provides ID, timestamps (createdAt, updatedAt), and optimistic locking (version field)
- Services contain business logic and are transactional
- Controllers are thin, delegating to services and handling HTTP concerns
- DTOs separate internal models from API contracts

**Security Architecture:**
- JWT-based authentication using `JwtUtil` for token generation/validation
- `JwtAuthenticationFilter` intercepts requests and validates tokens
- `SecurityConfig` defines endpoint access rules
- Public endpoints: `/api/v1/auth/**`, product GET operations, Swagger UI
- Protected endpoints require authentication; some require ADMIN role
- User entity implements `UserDetails` for Spring Security integration

**Database Management:**
- Flyway manages schema migrations (located in `src/main/resources/db/migration/`)
- Migration files follow naming: `V{version}__{description}.sql`
- JPA auditing enabled via `@EnableJpaAuditing` on main application class
- Hibernate validation mode set to `validate` (no auto-DDL changes)

**Order Processing:**
- Orders have a lifecycle: PENDING → CONFIRMED → PREPARING → OUT_FOR_DELIVERY → DELIVERED
- Orders can be CANCELLED before DELIVERED status
- Order totals are calculated from items: subtotal + tax + deliveryFee = totalAmount
- Each order item captures product price at time of order (price snapshot)
- Order items support customizations and special notes

## Important Configuration

### Database Connection
- Database: `coffee_shop_db` on PostgreSQL (localhost:5432)
- Credentials configured in `application.properties`
- Connection managed via Spring Data JPA

### JWT Configuration
- JWT secret and expiration configured in `application.properties`
- Default expiration: 86400000ms (24 hours)
- Tokens are validated on each request via `JwtAuthenticationFilter`

### API Documentation
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI docs: http://localhost:8080/v3/api-docs
- Configuration in `OpenAPIConfig` class

### Caching
- Spring Cache abstraction enabled via `@EnableCaching`
- Ready for cache provider integration (Redis, Caffeine, etc.)

## Development Notes

### Annotation Processors
The Maven compiler plugin is configured with annotation processor paths for:
- Lombok (code generation)
- MapStruct (DTO mapping)
- lombok-mapstruct-binding (integration between the two)

These must be processed in order. If you add or modify entities/DTOs, ensure the project is rebuilt to regenerate MapStruct mappers.

### Adding New Entities
1. Extend `BaseEntity` for audit fields and optimistic locking
2. Add JPA annotations (`@Entity`, `@Table`, etc.)
3. Use Lombok annotations (`@Getter`, `@Setter`, `@Builder`, etc.)
4. Create corresponding repository extending `JpaRepository`
5. Create DTOs for request/response
6. Implement service layer with `@Transactional` methods
7. Create controller with proper security annotations

### Database Migrations
- Never modify existing migration files
- Create new migration with incremented version number
- Test migrations on fresh database before committing
- Use `spring.flyway.baseline-on-migrate=true` for existing databases

### Security Considerations
- JWT secret in `application.properties` should use environment variable in production
- CORS currently allows all origins (`*`) - restrict in production
- Password encoding uses BCrypt via `PasswordEncoder` bean
- All authenticated endpoints receive the `User` principal from `Authentication`

### Error Handling
- `GlobalExceptionHandler` provides centralized exception handling
- Custom exceptions: `ResourceNotFoundException`, `BadRequestException`, `UnauthorizedException`
- Returns consistent error responses across the API

## API Endpoint Patterns

**Authentication:** `/api/v1/auth/**`
- Registration, login, token refresh

**Products:** `/api/v1/products/**`
- GET operations are public
- POST/PUT/DELETE require ADMIN role

**Orders:** `/api/v1/orders/**`
- All endpoints require authentication
- Users can only access their own orders
- Status updates may require ADMIN role

**Users:** `/api/v1/users/**`
- User profile management
- Requires authentication
