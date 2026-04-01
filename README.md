# DrivalTech Finance API

## Overview

### This is a financial management API built with Spring Boot, designed to simulate real-world backend scenarios such as:

- User authentication and authorization (JWT)
- Role-based access control (ADMIN / USER)
- Multi-tenant data isolation
- Transaction management
- Dashboard financial summary (income, expense, balance)

### The project follows clean architecture principles and focuses on best practices used in real backend systems.

---

## Visão Geral

### Esta é uma API de gerenciamento financeiro desenvolvida com Spring Boot, simulando cenários reais de backend, como:

- Autenticação e autorização com JWT
- Controle de acesso por perfil (ADMIN / USER)
- Isolamento de dados por usuário (multi-tenant)
- Gerenciamento de transações
- Resumo financeiro (receita, despesa e saldo)

### O projeto segue boas práticas de arquitetura e desenvolvimento backend profissional.

---

## Features

- JWT-based authentication and authorization
- Role-based access control (ADMIN / USER)
- Multi-tenant data isolation (user-specific data access)
- Full CRUD for transactions with pagination, sorting and filtering
- Category management (CRUD)
- Dashboard financial summary (income, expense, balance)
- Dynamic filtering (date range, category, transaction type)
- Pagination and sorting support (page, size, sort)
- Secure REST endpoints with Spring Security
- Global exception handling with standardized responses
- Request validation with Jakarta Validation (@Valid)
- Field-level error responses
- Custom enum validation

### 🔍 Logging & Observability

The API includes a logging interceptor to monitor all HTTP requests and responses.

Features:
- Logs HTTP method and endpoint
- Tracks response status codes
- Measures request execution time
- Helps debugging and monitoring in development and production environments

Example log:

[REQUEST] POST /transactions  
[RESPONSE] POST /transactions | status=201 | duration=73ms

---

## Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- PostgreSQL
- Maven

---

## Architecture

### The project is structured following layered architecture:

#### controller → service → repository → database

### Additional layers:
- `dto` – data transfer objects for request/response separation
- `exception` – centralized global error handling
- `security` – authentication and authorization (JWT)
- `specification` – dynamic query filtering (JPA Specifications)
- `pagination` – standardized paginated API responses
---

## Security

- JWT-based authentication
- Role-based authorization (ADMIN / USER)
- Endpoint protection using `@PreAuthorize`
- Multi-tenant filtering (users access only their own data)
- Stateless authentication (no server-side sessions)

---

## Dashboard

- `GET /dashboard/summary`
- `GET /dashboard/summary?type=INCOME|EXPENSE`
- `GET /dashboard/summary?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD`
- `GET /dashboard/summary?categoryId=UUID`
- `GET /dashboard/summary?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD&categoryId=UUID&type=INCOME|EXPENSE`

### Optional filters:

- `type=INCOME|EXPENSE`
- `startDate=YYYY-MM-DD`
- `endDate=YYYY-MM-DD`
- `categoryId=UUID`

### Example Request:

#### GET /dashboard/summary?startDate=2026-01-01&endDate=2026-03-31&type=INCOME

### Dashboard Example:

```json
{
  "income": 5000.00,
  "expense":3000.00,
  "balance":2000.00
}
```

### Dashboard Analytics:

- `GET /dashboard/analytics?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD`

#### Returns financial insights including:

- Current vs previous period comparison
- Income, expense and balance growth (%)

---

## Main Endpoints

### Auth
- `POST /auth/login`

---

### Users
- `POST /users`
- `GET /users`
- `PUT /users/{id}`
- `PATCH /users/{id}/deactivate`

> Note: User deletion is handled via deactivation (soft delete).
---

### Transactions
- `POST /transactions`
- `GET /transactions`
- `PUT /transactions/{id}`
- `DELETE /transactions/{id}`

### Transactions (Pagination, Sorting & Filtering)

#### Query Parameters:

- `page` (default: 0)
- `size` (default: 10)
- `sort=field,asc|desc` (e.g., `date,desc`)
- `type=INCOME|EXPENSE`

#### Examples Requests:

- `GET /transactions?page=0&size=5`
- `GET /transactions?sort=date,desc`
- `GET /transactions?type=INCOME`
- `GET /transactions?page=0&size=5&sort=amount,asc&type=EXPENSE`
---

## How to Run

### 1. Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/drivaltech-finance-api.git
```

### 2. Configure database

### Update `application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/drivaltech
    username: postgres
    password: your_password
```
### 3. Run the project:
###   ./mvnw spring-boot:run

---
   
## Testing

### You can test the API using:

### Postman   
### Insomnia

---

## Author

## Valter
### Backend Developer in progress 

---

## Future Improvements
- Advanced dashboard analytics (charts, trends, KPIs)
- Redis caching for performance optimization
- Docker containerization
- CI/CD pipeline integration
- Frontend integration (React or similar)
- API rate limiting and monitoring


