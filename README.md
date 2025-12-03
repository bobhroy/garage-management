# 🚗 Garage Management System

A **Spring Boot microservices project** for managing a garage that handles various vehicle repair service requests.

---

## 🏗️ Overview

The Garage Management System is composed of several Spring Boot services that work together to support end-to-end garage operations — from customer profile creation to service checkout and invoice generation. It also includes gRPC and Kafka-based inter-service communication.

---

## 🧩 System Architecture

    [ User Service ] ←→ [ API Gateway ] ←→ [ Garage Service ] ←→ [ Billing Service ]
                                                 ↓
                                               (Kafka)
                                                 ↓
                                         [ Analytical Service ]


**Key highlights:**
- **gRPC Communication:** Garage ↔ Billing  
- **Kafka Integration:** Garage → Analytical  
- **JWT Authentication:** Managed by User Service  
- **Role-Based Access Control:** Managed by User Service  
- **Gateway Filtering:** All external requests routed through API Gateway

---

## ⚙️ Tech Stack

| Layer | Technologies |
|-------|---------------|
| Backend | Java 21, Spring Boot 3.x, Spring WebFlux, Spring Data JPA |
| Communication | gRPC, Apache Kafka |
| Database | MySQL |
| Security | Spring Security, JWT |
| Build Tool | Maven |
| DevOps | Docker, Docker Compose |
| Optional | Kubernetes, Liquibase/Flyway for DB migration |

---

## 🧰 Services Overview

### 1. 🧰 Garage Service
**Description:**  
Manages customer profiles, carts, service requests, and checkouts.  
Acts as:
- **gRPC client** to the Billing Service  
- **Kafka producer** for the Analytical Service  

**Features:**
- Create and manage customer profiles  
- Add services to a cart  
- Checkout and generate invoice requests  
- Secure endpoints (requires valid JWT token)

**Environment Variables:**
```env
BILLING_SERVICE_ADDRESS=billing-service
BILLING_SERVICE_GRPC_PORT=9001
SPRING_DATASOURCE_URL=jdbc:mysql://garage-service-db:3306/db?createDatabaseIfNotExist=true
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=MyP@ssword!
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
SPRING_SQL_INIT_MODE=always
```
🗄️ Garage Service Database

The Garage Service uses a MySQL container as its persistent data store.

Database Environment Variables:
```env
MYSQL_DATABASE=db
MYSQL_ROOT_PASSWORD=MyP@ssword!
```

### 2. 💳 Billing Service
**Description:**  
Processes payments and generates invoices based on checkout requests from Garage Service.  
Acts as:
- **gRPC server** for Garage Service.

**Features:**
- Create billing accounts  
- Simulate payment processing  
- Generate invoices  
- Internal service (no public API)

**Environment Variables:**
```env
SPRING_DATASOURCE_URL=jdbc:mysql://garage-service-db:3306/db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=MyP@ssword!
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_SQL_INIT_MODE=always
```
### 3. 📊 Analytical Service
**Description:**  
Consumes Kafka messages from the Garage Service and updates loyalty points for customers upon successful payment.

**Features:**
- Kafka consumer for payment events  
- Simulated loyalty points management  
- Internal service (no public API)

**Environment Variables:**
```env
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```
### 4. 👥 User Service
**Description:**  
Manages users of the Garage Management System — such as garage employees, administrators, or owners.  
Handles **authentication**, **authorization**, and **role-based access control** (RBAC) for all other services.

**Features:**
- Create, update, and delete user accounts  
- Manage user roles (e.g., ADMIN, USER)  
- Issue and validate **JWT tokens**  
- Integrates with API Gateway for authentication  
- Restricts API access based on roles and permissions  

**Environment Variables:**
```env
SPRING_DATASOURCE_URL=jdbc:mysql://user-service-db:3306/user_db?createDatabaseIfNotExist=true
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=MyP@ssword!
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_SQL_INIT_MODE=always
```
.env File (Required):

⚠️ Note:
You must provide a .env file in the root of the User Service project.
This file defines the master secret key used for JWT signing and validation.
The secret should be a securely generated, hashed, or long random key.

**Master secret key for encoding and verifying JWT tokens**

JWT_SECRET=<put_a_hashed_key_for_encoding>

🗄️ User Service Database

The User Service uses a dedicated MySQL database container to manage user data and authentication details.

Database Environment Variables:
```env
MYSQL_DATABASE=user_db
MYSQL_ROOT_PASSWORD=MyP@ssword!
```

### 5. 🌐 API Gateway
**Description:**  
The **API Gateway** acts as the single entry point for all client requests.  
It handles **authentication**, **authorization**, and **routing** to backend microservices such as Garage, Billing, and User Services.  

This service ensures that:
- Only authenticated users can access protected routes.
- User tokens are validated via the **User Service** before requests are forwarded.
- Unauthenticated or expired tokens result in access denial (HTTP 401 or 403).

**Features:**
- Centralized API routing and access control  
- Token validation filter (custom `JwtValidationGatewayFilterFactory`)  
- Integration with Spring Cloud Gateway for route management  
- Secure communication between microservices  
- External clients only communicate through the gateway

**Environment Variables:**
```env
USER_SERVICE_URL=http://user-service:4003
```
**Authentication Flow:**

A client logs in via the User Service to obtain a JWT token.

For each request to protected resources (e.g., /api/garage/**),
the client includes the token in the Authorization header:

Authorization: Bearer <jwt-token>


The API Gateway intercepts the request using a custom filter:

Calls USER_SERVICE_URL/auth/validate

If valid → forwards request to the target service

If invalid → returns 401 Unauthorized

The target microservice (e.g., Garage Service) never directly exposes its endpoints to external clients.

### 6. 🪄 Kafka Configuration
**Description:**  
Apache Kafka is used as the **event streaming backbone** between the microservices.  
It allows the system to process asynchronous events — for example, when an order is completed in the Garage Service, an event is published that triggers loyalty point updates in the Analytical Service.

**Purpose in the System:**
- **Garage Service:** Produces messages (e.g., `ORDER_COMPLETED`, `INVOICE_GENERATED`)  
- **Analytical Service:** Consumes messages and updates loyalty points or analytics  
- **Billing Service (Optional):** Can produce audit or billing confirmation events  
- Enables **event-driven architecture** and **loose coupling** between services

**Environment Variables:**
```env
KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092,EXTERNAL://localhost:9094
KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER
KAFKA_CONTROLLER_QUORUM_VOTERS=1@kafka:9093
KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,EXTERNAL:PLAINTEXT,PLAINTEXT:PLAINTEXT
KAFKA_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093,EXTERNAL://:9094
KAFKA_NODE_ID=1
KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1
KAFKA_PROCESS_ROLES=broker,controller
```







