# 🛒 Order Service - TechGear Platform

The **Order Service** handles order management, invoice/report PDF generation, Stripe-based payments, AI chatbot assistance, and QR code creation. It's part of the TechGear e-commerce microservices system.

---

## 🚀 Key Features

- CRUD operations for orders
- PDF generation for:
  - **Invoices** (per order)
  - **Daily/custom reports**
- QR Code generation for orders
- AI Assistant (via Cohere API)
- Stripe payment session integration
- Admin email notifications with attachments

---

## ⚙️ Tech Stack

- Java 17, Spring Boot
- MySQL, JPA/Hibernate
- Stripe SDK
- iText PDF, ZXing (QR code)
- Email API (Gmail SMTP)
- Docker-ready
- Eureka Client
- Optional Config Server

---

## 🧪 API Overview

### 📦 Order Management

| Method | Endpoint                 | Description                |
|--------|--------------------------|----------------------------|
| POST   | `/api/orders`            | Create new order           |
| GET    | `/api/orders`            | Get all orders             |
| GET    | `/api/orders/{id}`       | Get order by ID            |
| DELETE | `/api/orders/{id}`       | Delete order               |

### 📄 PDF Generation

| Method | Endpoint                                 | Description                      |
|--------|------------------------------------------|----------------------------------|
| GET    | `/api/orders/{id}/invoice`               | Generate invoice PDF             |
| GET    | `/api/orders/reports/daily`              | Daily orders summary report      |
| GET    | `/api/orders/reports/custom`             | Custom date-range report         |

> ✅ Optional: `sendToAdmin=true` sends report to admin via email.

### 💳 Payments

| Method | Endpoint                              | Description                  |
|--------|----------------------------------------|------------------------------|
| POST   | `/api/payment/create-checkout-session` | Create Stripe checkout URL   |
| GET    | `/api/payment/success`                | Payment success callback     |
| GET    | `/api/payment/cancel`                 | Payment cancel callback      |

### 🤖 AI Chat Assistant

| Method | Endpoint             | Description              |
|--------|----------------------|--------------------------|
| POST   | `/api/orders/ask-ai` | Ask question to AI bot   |

### 🔳 QR Code

| Method | Endpoint             | Description           |
|--------|----------------------|-----------------------|
| GET    | `/api/orders/{id}/qr`| Generate QR code PNG  |

---

## 🛠️ Configuration

Set in `application.properties`:

```properties
# Service info
server.port=8082
spring.application.name=order-service

# Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true

# MySQL DB
spring.datasource.url=jdbc:mysql://localhost:3306/ms-order
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update

# Config server (optional)
spring.config.import=optional:configserver:http://localhost:8887

# Stripe
stripe.secret.key=sk_test_...
stripe.success.url=http://localhost:8093/api/payment/success
stripe.cancel.url=http://localhost:8093/api/payment/cancel

# Gmail SMTP
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password

# AI (Cohere)
COHERE_API_KEY=your_cohere_key
```

### 🐳 Docker (optional)
```bash
docker build -t order-service .
```
```bash
docker run -p 8082:8082 order-service
```

## Project Structure

```
src/
├── api/                    # REST controllers
├── dto/                    # Request/response DTOs
├── entities/               # JPA entities
├── services/               # Business logic & integrations
├── resources/
│   ├── static/logo.png     # Used in invoice PDFs
│   └── application.properties

```

### 📎 Dependencies
- iText for PDFs
- ZXing for QR codes
- Stripe SDK
- Spring Mail
- Cohere AI


## 📬 Contact
📧 ayari.hamza1@esprit.tn
