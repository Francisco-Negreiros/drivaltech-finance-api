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

## Features

### Security
- JWT-based authentication and authorization
- Role-based access control (ADMIN / USER)
- Multi-tenant data isolation

### Transactions
- Full CRUD operations
- Pagination, sorting, and filtering
- Category association

### Dashboard
- Financial summary (income, expense, balance)
- Dynamic filters (date range, category, type)

### Infrastructure
- Global exception handling
- Request validation (@Valid)
- Standardized error responses
- Custom enum validation

---

## Security

- JWT-based authentication
- Role-based authorization (ADMIN / USER)
- Endpoint protection using `@PreAuthorize`
- Multi-tenant filtering (users access only their own data)
- Stateless authentication (no server-side sessions)

--- 

## Logging & Observability

- The API implements advanced logging with full request traceability using correlation ID, user context, and client IP.

- Correlation ID
- Each request receives a unique identifier (UUID)
- Propagated across all logs using MDC

### User Context
- Authenticated username is extracted from Spring Security
- Automatically included in all logs via MDC

### Client IP
- Captured from HttpServletRequest
- Supports X-Forwarded-For for proxy environments

### Example Logs
- [a37bee74...] [user=valter] [ip=127.0.0.1] - [REQUEST] POST /transactions
- [a37bee74...] [user=valter] [ip=127.0.0.1] - Creating transaction ...
- [a37bee74...] [user=valter] [ip=127.0.0.1] - [RESPONSE] POST /transactions | status=201 | duration=311ms

### Implementation Details
- LoggingInterceptor handles request lifecycle
- MDC (Mapped Diagnostic Context) stores:
  - correlationId
  - username
  - ip
- Logging pattern configured in application.yaml

### Benefits
- Full traceability (request → user → origin)
- Easier debugging in concurrent environments
- Production-ready observability
- Improved monitoring and auditing capabilities

---

## Audit Trail

- The API implements a persistent audit trail system to track user actions on key resources.

### What is recorded
- User ID
- Action performed (CREATE, UPDATE, DELETE)
- Resource affected (e.g., TRANSACTION)
- Resource ID
- Client IP address
- Timestamp

### Example Records
- User f7903d46... CREATED TRANSACTION bd040272...
- User f7903d46... DELETED TRANSACTION 123...

### Implementation Details
- Audit logs are stored in tb_audit_log
- AuditService centralizes audit logic
- Logging is non-blocking (fail-safe with try/catch)
- Timestamp is automatically generated using @PrePersist

### Benefits
- Full traceability of user actions
- Improved debugging and monitoring
- Foundation for compliance and auditing
- Production-ready backend pattern

--- 
## Rate Limiting

To protect the API from excessive usage and abuse, a rate limiting mechanism has been implemented.

### Configuration

```yaml
rate-limit:
  max-requests: 5
  time-window: 60000
```

* **max-requests**: Maximum number of requests allowed
* **time-window**: Time window in milliseconds

### Behavior

When the limit is exceeded:

* The API returns **HTTP 429 - Too Many Requests**
* A standardized JSON response is returned:

```json
{
  "status": 429,
  "error": "Too Many Requests",
  "message": "Rate limit exceeded. Try again later."
}
```

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

### Logging

Blocked requests are logged with:

* IP address
* Request count

Example:
```
Rate limit exceeded | ip=127.0.0.1 | count=6
```
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
### Requirements

- Java 17+
- PostgreSQL

### Run

```bash
./mvnw spring-boot:run
```
---
   
## Testing

### You can test the API using:

- Postman or Insomnia for manual testing
- REST clients with JWT authentication

### Automated tests (JUnit & Mockito) are planned for future implementation.

---

## Author

## Valter
### Backend Developer (Java & Spring Boot)

---

## Future Improvements

### Observability & Monitoring
- Integration with monitoring tools (ELK Stack / Grafana)
- Correlation ID propagation across services (microservices readiness)

### Performance & Scalability
- Redis caching for frequently accessed data
- Database query optimization

### Testing & Quality
- Unit tests with JUnit and Mockito
- Integration tests for REST endpoints
- Test coverage reporting

### DevOps & Deployment
- Docker containerization
- CI/CD pipeline integration
- Environment-based configuration (dev/staging/prod)

### Product Evolution
- Advanced dashboard analytics (charts, trends, KPIs)
- Frontend integration (React or similar)


