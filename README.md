# Coffee Shop Backend API

A robust Spring Boot REST API for a mobile coffee shop application, providing comprehensive endpoints for user authentication, product management, order processing, and user profile management.

## Features

- **User Authentication & Authorization**: JWT-based authentication with role-based access control (USER, ADMIN)
- **Product Management**: Full CRUD operations for coffee products with categories, sizes, and pricing
- **Order Processing**: Complete order lifecycle management with status tracking
- **User Management**: User registration, profile management, and address handling
- **Product Reviews**: Rating and review system for products
- **Database Migrations**: Version-controlled schema management using Flyway
- **API Documentation**: Interactive API documentation with Swagger/OpenAPI
- **Security**: BCrypt password encryption, JWT token validation, and Spring Security integration

## Tech Stack

- **Framework**: Spring Boot 3.5.7
- **Language**: Java 17
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA with Hibernate
- **Security**: Spring Security with JWT
- **Database Migration**: Flyway
- **DTO Mapping**: MapStruct
- **Code Simplification**: Lombok
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Build Tool**: Maven
- **Containerization**: Docker Compose for PostgreSQL and pgAdmin

## Prerequisites

Before running this application, make sure you have the following installed:

- Java 17 or higher
- Maven 3.6+ (or use the included Maven Wrapper)
- Docker and Docker Compose (for database)
- Git

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/Kh-Ashraf-Dev/Coffee-shop-backend.git
cd Coffee-shop-backend
```

### 2. Start the Database

Start PostgreSQL and pgAdmin using Docker Compose:

```bash
docker-compose up -d
```

**Database Connection Details:**
- Host: `localhost:5432`
- Database: `coffee_shop_db`
- Username: `postgres`
- Password: `postgres`

**pgAdmin Access:**
- URL: http://localhost:5050
- Email: `admin@coffeeshop.com`
- Password: `admin`

### 3. Build the Project

**Windows:**
```bash
mvnw.cmd clean install
```

**Unix/Linux/Mac:**
```bash
./mvnw clean install
```

### 4. Run the Application

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

**Unix/Linux/Mac:**
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## Configuration

The application is configured through `src/main/resources/application.properties`. Key configurations include:

- **Database Connection**: PostgreSQL connection settings
- **JWT Configuration**: Secret key and token expiration time
- **Flyway**: Database migration settings
- **Hibernate**: Validation mode (no auto-DDL)
- **Server Port**: Default 8080

For production deployment, use environment variables to override sensitive configurations like JWT secret and database credentials.

## API Documentation

Once the application is running, you can access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### API Endpoints Overview

#### Authentication (`/api/v1/auth`)
- `POST /register` - Register a new user
- `POST /login` - Login and receive JWT token

#### Products (`/api/v1/products`)
- `GET /products` - Get all products (Public)
- `GET /products/{id}` - Get product by ID (Public)
- `POST /products` - Create product (Admin only)
- `PUT /products/{id}` - Update product (Admin only)
- `DELETE /products/{id}` - Delete product (Admin only)

#### Orders (`/api/v1/orders`)
- `POST /orders` - Create a new order (Authenticated)
- `GET /orders` - Get user's orders (Authenticated)
- `GET /orders/{id}` - Get order details (Authenticated)
- `PUT /orders/{id}/status` - Update order status (Admin only)

#### Users (`/api/v1/users`)
- `GET /users/profile` - Get user profile (Authenticated)
- `PUT /users/profile` - Update user profile (Authenticated)

## Database Schema

The database schema is managed through Flyway migrations located in `src/main/resources/db/migration/`:

- `V1__Initial_Schema.sql` - Creates all tables and relationships
- `V2__Sample_Data.sql` - Inserts sample products and test users

### Main Entities

- **User**: User accounts with authentication details
- **Product**: Coffee products with categories and pricing
- **Order**: Customer orders with status tracking
- **OrderItem**: Individual items within an order
- **Address**: User delivery addresses
- **Review**: Product reviews and ratings

## Project Structure

```
src/
├── main/
│   ├── java/com/coffeeshop/
│   │   ├── config/           # Configuration classes
│   │   ├── controller/       # REST controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # JPA entities
│   │   ├── enums/           # Enumerations
│   │   ├── exception/       # Custom exceptions
│   │   ├── repository/      # Spring Data repositories
│   │   ├── security/        # Security components
│   │   └── service/         # Business logic
│   └── resources/
│       ├── application.properties
│       └── db/migration/    # Flyway migrations
└── test/                    # Unit and integration tests
```

## Testing

Run all tests:

```bash
mvnw.cmd test
```

Run specific test class:

```bash
mvnw.cmd test -Dtest=CoffeeShopBackendApplicationTests
```

## Security

- **Password Encryption**: BCrypt hashing algorithm
- **JWT Tokens**: Stateless authentication with configurable expiration
- **Role-Based Access**: USER and ADMIN roles with endpoint-level security
- **CORS**: Currently configured to allow all origins (configure for production)

## Sample Data

The application includes sample data for testing:

**Test Users:**
- Admin: `admin@coffeeshop.com` / `admin123`
- User: `john.doe@example.com` / `password123`

**Sample Products:**
- Various coffee types (Espresso, Cappuccino, Latte, etc.)
- Different sizes and prices

## Order Status Flow

Orders follow this lifecycle:
1. **PENDING** - Order created
2. **CONFIRMED** - Order confirmed by admin
3. **PREPARING** - Coffee being prepared
4. **OUT_FOR_DELIVERY** - Order out for delivery
5. **DELIVERED** - Order completed
6. **CANCELLED** - Order cancelled (before delivery)

## Development

### Adding New Features

1. Create/modify entities in `entity/` package
2. Create corresponding repositories in `repository/` package
3. Implement business logic in `service/` package
4. Create DTOs in `dto/` package
5. Create REST controllers in `controller/` package
6. Add database migrations if schema changes
7. Update tests

### Code Style

- Use Lombok annotations to reduce boilerplate
- Follow REST best practices for API design
- Use MapStruct for entity-DTO mapping
- Write transactional service methods
- Implement proper exception handling

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contact

For questions or support, please open an issue in the GitHub repository.

## Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL for the robust database
- All contributors and users of this project
