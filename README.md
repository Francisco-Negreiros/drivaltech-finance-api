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

## Redis Caching

### Overview

#### This project uses Redis as a distributed cache to improve performance of the dashboard summary endpoint.

### Configuration

```
spring:
  data:
    redis:
      host: localhost
      port: 6379
```
### Running Redis with Docker
```
docker run -d -p 6379:6379 redis
```
### Cache Strategy
- The endpoint /dashboard/summary is cached using Spring Cache
- Cache is based on:
  - method parameters
  - authenticated user ID

#### Example:
```
dashboard_getSummary_2026-04-01_2026-04-30_null_INCOME_userId
```
### Cache Isolation

#### Each user has its own cache entry, preventing data leakage between users.

### Cache Eviction

#### Cache is automatically invalidated when:

- transaction is created
- transaction is updated
- transaction is deleted

#### Handled via:
```
@CacheEvict(value = "dashboard", allEntries = true)
```

### Benefits
- Faster responses for repeated requests
- Reduced database load
- Better scalability

### Notes

- Ensure Redis is running before starting the application
- Default serialization uses Java serialization (Serializable)
- For production, consider using JSON serialization (Jackson)

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

## Implemented Features

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
- Redis caching for frequently accessed data
- Metrics and monitoring with Prometheus and Grafana

---

## Observability

### Metrics Exposure
#### The API exposes metrics via:

```
GET /actuator/prometheus
```

#### Metrics are collected using Micrometer and include:
- HTTP requests (count, latency, errors)
- JVM metrics
- Database connection metrics
- Cache metrics (Redis)

### Prometheus

#### Prometheus is used to scrape and store application metrics.

#### Run with Docker:
```
docker run -d -p 9090:9090 \
-v ./prometheus:/etc/prometheus \
prom/prometheus
```

#### Access:

```
http://localhost:9090
```

### Grafana

#### Grafana is used for metrics visualization.

#### Run with Docker:

```
docker run -d -p 3000:3000 --name grafana grafana/grafana
```

#### Access:

```
http://localhost:3000
```
- Default credentials:
- admin / admin

### Dashboard

#### The observability dashboard includes:

- Throughput (requests per second)
- Latency (average response time)
- Error rate (HTTP 5xx)
- Top endpoints (most accessed routes)

---

## Security

- JWT-based authentication
- Role-based authorization (ADMIN / USER)
- Endpoint protection using `@PreAuthorize`
- Multi-tenant filtering (users access only their own data)
- Stateless authentication (no server-side sessions)

### JWT Configuration

#### JWT settings are externalized via `application.yaml`:

```
jwt:
  secret: your-secret-key
  expiration: 3600000
```

- secret: signing key used to generate and validate tokens
- expiration: token validity time in milliseconds

#### This allows environment-based configuration (dev, staging, production).

--- 

## Caching Strategy (Redis)

#### The application uses Redis as a caching layer to improve performance and reduce database load.

### Dashboard Caching

#### The endpoint `/dashboard/summary` is cached using Spring Cache abstraction.

### Cache Key Structure

#### Cache keys are composed of:

- method name
- request parameters (startDate, endDate, categoryId, type)
- authenticated user ID

#### Example:

```
dashboard::getSummary_2026-04-01_2026-04-30_null_INCOME_userId
```

### TTL (Time-To-Live)

- Cache entries expire automatically after **5 minutes**
- Prevents stale data and ensures periodic refresh

### Cache Invalidation

#### Cache is automatically cleared when transactions are modified:

- Create transaction
- Update transaction
- Delete transaction

#### Implemented using:

```
@CacheEvict(value = "dashboard", allEntries = true)
```
### Benefits
- Faster response times
- Reduced database load
- Improved scalability

---

## Observability & Metrics

#### The API implements production-level observability using Spring Boot Actuator and Micrometer.

### Health Check

#### Endpoint:
```
GET /actuator/health
```

### Provides detailed application status, including:

- Database (PostgreSQL)
- Redis
- Disk space
- SSL

#### Example:

```
{
"status": "UP"
}
```

### Metrics

#### Endpoint:

```
GET /actuator/metrics
```

#### Provides a list of available metrics such as:

- JVM memory and GC
- CPU usage
- Database connection pool (HikariCP)
- HTTP request metrics
- Spring Security metrics

### HTTP Metrics

#### Endpoint:

```
GET /actuator/metrics/http.server.requests
```

#### Tracks:

- Request count
- Response time
- HTTP status codes
- Endpoint usage

#### Example insights:

- Performance per endpoint
- Error rate monitoring
- Request distribution



### Prometheus Integration (Ready)

#### Endpoint:

```
GET /actuator/prometheus
```

#### Exposes metrics in a format compatible with Prometheus.



### Observability Capabilities

- Real-time health monitoring
- Performance tracking
- Error analysis
- Infrastructure visibility
- Ready for dashboards and alerting (Grafana + Prometheus)

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

#### To protect the API from excessive usage and abuse, a rate limiting mechanism has been implemented.

### Configuration

```yaml
rate-limit:
  max-requests: 5
  time-window: 60000
```

* **max-requests**: Maximum number of requests allowed
* **time-window**: Time window in milliseconds

### Behavior

#### When the limit is exceeded:

* The API returns **HTTP 429 - Too Many Requests**
* A standardized JSON response is returned:

```json
{
  "status": 429,
  "error": "Too Many Requests",
  "message": "Rate limit exceeded. Try again later."
}
```
### Purpose

#### This mechanism helps protect the API against abuse, brute force attempts, and excessive usage,ensuring system stability and fair usage.

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

#### Blocked requests are logged with:

* IP address
* Request count

#### Example:
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

### The project includes unit tests to ensure business rule correctness and system reliability.

### Covered scenarios:

- Transaction creation (success case)
- Authorization validation (multi-tenant rules)
- Forbidden operations (accessing чуж data)
- Update and delete validations
- Edge cases and invalid inputs

### Tools used:

- JUnit 5
- Mockito

### Key points:

- SecurityContext is mocked to simulate authenticated users
- Repository interactions are isolated using mocks
- Audit logging is designed as fail-safe (does not break main flow)

### How to run tests:

```bash
./mvnw test
```
---

## Author

## Valter
### Backend Developer (Java & Spring Boot)

---

## Future Improvements

### Observability & Monitoring
- Alerting with Prometheus (custom rules and thresholds)
- Grafana dashboards with alerts and notifications
- Distributed tracing with OpenTelemetry (end-to-end request tracking)
- Centralized logging with ELK Stack (Elasticsearch, Logstash, Kibana)
- SLA/SLO monitoring and error budget tracking

### Performance & Scalability
- Cache TTL and eviction strategies (Redis)
- Database query optimization and indexing strategies
- Horizontal scalability (stateless API + load balancing ready)
- Connection pool tuning (HikariCP)

### Testing & Quality
- Unit tests with JUnit and Mockito
- Integration tests for REST endpoints
- Test coverage reporting (JaCoCo)
- Testcontainers for integration testing with real dependencies

### DevOps & Deployment
- Docker containerization (API + Redis + Prometheus + Grafana)
- Docker Compose for local environment orchestration
- CI/CD pipeline integration
- Environment-based configuration (dev/staging/prod)

### Product Evolution
- Advanced dashboard analytics (charts, trends, KPIs)
- Frontend integration (React or similar)
- Multi-user financial insights and reports
- Export features (CSV/PDF reports)


