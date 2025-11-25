# Alfa API

Spring Boot REST API with Employee and User management.

## Tech Stack

- Java 17
- Spring Boot 3
- MySQL
- JPA/Hibernate
- Lombok

## Prerequisites

- JDK 17+
- Maven 3.8+
- MySQL

## Setup

```bash
# Set environment variables
export DB_PASSWORD=your_password

./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## Docker

```bash
docker build -t alfa-api .
docker run -e DB_PASSWORD=secret -p 8080:8080 alfa-api
```

## API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/users` | GET | List users |
| `/api/employees` | GET | List employees |

## Configuration

Set via environment variables:
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
