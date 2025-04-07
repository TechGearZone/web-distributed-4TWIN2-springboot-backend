# TechGear Microservices Architecture

This project demonstrates a secure microservices architecture using Spring Boot, Spring Cloud, and Docker, with Keycloak for authentication and authorization.

## Architecture

The project consists of the following microservices:

1. **Eureka Server** - Service discovery server (Port: 8761)
2. **User Service** - User management service with role-based access control (Port: 8081)
3. **API Gateway** - Gateway for routing and token relay (Port: 8093)
4. **Keycloak** - Identity and access management (Port: 8080)
5. **MySQL** - Database for user service (Port: 3306)
6. **PostgreSQL** - Database for Keycloak (Port: 5432)

## Security Features

- OAuth2/OpenID Connect authentication with Keycloak
- JWT token-based authorization
- Role-based access control (ADMIN role required for certain operations)
- Token relay through API Gateway to microservices
- Secure communication between services

## Prerequisites

- Docker and Docker Compose
- Java 17
- Maven

## Building the Project

1. Build all services:

```bash
# Build Eureka Server
cd eureka-server
mvn clean package -DskipTests

# Build User Service
cd ../user-service
mvn clean package -DskipTests

# Build API Gateway
cd ../api-gateway
mvn clean package -DskipTests
```

2. Run with Docker Compose:

```bash
cd ..
docker-compose up -d
```

## Initial Setup

1. Access Keycloak admin console at http://localhost:8080/admin (admin/admin)
2. Create a new realm called "techgear"
3. Create a client:
   - Client ID: techgear-client
   - Access Type: confidential
   - Valid Redirect URIs: http://localhost:8093/*
4. Create roles:
   - ADMIN
   - USER
5. Create a test user and assign roles

## API Endpoints

### Public Endpoints

#### Register a New User
```http
POST http://localhost:8093/api/users/register
Content-Type: application/json

{
  "email": "test@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "password": "password123",
  "phoneNumber": "1234567890",
  "address": "123 Main St"
}
```

### Protected Endpoints (Requires Authentication)

#### Get All Users (Requires ADMIN role)
```http
GET http://localhost:8093/api/users
Authorization: Bearer <token>
```

#### Get User by ID (Requires ADMIN role or user's own ID)
```http
GET http://localhost:8093/api/users/{id}
Authorization: Bearer <token>
```

#### Update User (Requires ADMIN role or user's own ID)
```http
PUT http://localhost:8093/api/users/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "firstName": "Updated",
  "lastName": "Name",
  "phoneNumber": "9876543210",
  "address": "456 New St"
}
```

#### Delete User (Requires ADMIN role)
```http
DELETE http://localhost:8093/api/users/{id}
Authorization: Bearer <token>
```

### Authentication

#### Get Access Token
```http
POST http://localhost:8080/realms/techgear/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

username=admin&password=admin123&grant_type=password&client_id=techgear-client&client_secret=your_client_secret
```

## Service URLs

- **Eureka Dashboard**: http://localhost:8761
- **User Service**: http://localhost:8081
- **API Gateway**: http://localhost:8093
- **Keycloak Admin Console**: http://localhost:8080/admin

## Project Structure

```
MyProjectSoFar/
├── api-gateway/                # API Gateway service
│   ├── src/
│   └── pom.xml
├── eureka-server/             # Eureka Server for service discovery
│   ├── src/
│   └── pom.xml
├── user-service/              # User management service
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/techgear/services/user/
│   │       │       ├── controller/    # REST endpoints
│   │       │       ├── service/       # Business logic
│   │       │       ├── repository/    # Data access
│   │       │       ├── model/         # Domain models
│   │       │       └── config/        # Security config
│   │       └── resources/
│   └── pom.xml
├── docker-compose.yml         # Docker Compose configuration
└── README.md
```

## Stopping the Services

```bash
docker-compose down
```

## Technologies Used

- Spring Boot 3.x
- Spring Cloud (Eureka, Gateway)
- Spring Security with OAuth2
- Keycloak 23.0.6
- MySQL 8.0
- PostgreSQL 13
- Docker & Docker Compose
- Java 17
- Maven