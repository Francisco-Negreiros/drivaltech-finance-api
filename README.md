# 💰 DrivalTech Finance API

## 🇺🇸 Overview

This is a financial management API built with Spring Boot, designed to simulate real-world backend scenarios such as:

- User authentication and authorization (JWT)
- Role-based access control (ADMIN / USER)
- Multi-tenant data isolation
- Transaction management
- Dashboard financial summary (income, expense, balance)

The project follows clean architecture principles and focuses on best practices used in real backend systems.

---

## 🇧🇷 Visão Geral

Esta é uma API de gerenciamento financeiro desenvolvida com Spring Boot, simulando cenários reais de backend, como:

- Autenticação e autorização com JWT
- Controle de acesso por perfil (ADMIN / USER)
- Isolamento de dados por usuário (multi-tenant)
- Gerenciamento de transações
- Resumo financeiro (receita, despesa e saldo)

O projeto segue boas práticas de arquitetura e desenvolvimento backend profissional.

---

## 🚀 Features

- ✅ User authentication (JWT)
- ✅ Role-based authorization
- ✅ Multi-tenant data isolation
- ✅ CRUD for transactions
- ✅ Category management
- ✅ Dashboard summary (income / expense / balance)
- ✅ Secure endpoints with Spring Security
- ✅ Global exception handling

---

## 🛠 Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- PostgreSQL
- Maven

---

## 🧱 Architecture

The project is structured following layered architecture:

controller → service → repository → database

## Additional layers:
- `dto` for data transfer
- `exception` for global error handling
- `security` for authentication and authorization

---

## 🔐 Security

- JWT-based authentication
- Role-based authorization (ADMIN / USER)
- Endpoint protection using `@PreAuthorize`
- Multi-tenant filtering (users access only their own data)

---

## 📊 Dashboard Example

```json
{
  "income": 5000.00,
  "expense": 1000.00,
  "balance": 4000.00
}
```
---

## 📡 Main Endpoints

### 🔐 Auth
- `POST /auth/login`

---

### 👤 Users
- `POST /users`
- `GET /users`

---

### 💸 Transactions
- `POST /transactions`
- `GET /transactions`
- `PUT /transactions/{id}`
- `DELETE /transactions/{id}`

---

### 📊 Dashboard
- `GET /dashboard/summary`
- `GET /dashboard/summary?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD`

---

## ⚙️ How to Run

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
   
## 🧪 Testing

### You can test the API using:

### Postman   
### Insomnia

---

## 👨‍💻 Author

## Valter
### Backend Developer in progress 🚀

---

## 📌 Future Improvements
### Dashboard with filters (date range, category)   
### Redis caching   
### Docker support   
### CI/CD pipeline   
### Frontend integration


