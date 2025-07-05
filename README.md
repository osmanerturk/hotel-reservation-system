
# 🏨 Hotel Reservation System - Microservices Architecture

A complete hotel reservation system built using **Java 17**, **Spring Boot**, and **microservices architecture**. It supports hotel and room management, reservation handling, Kafka-based notifications, and an API Gateway for routing and authentication.

---

## 📦 Project Structure

```
hotel-reservation-system/
│
├── api-gateway/               # Authentication and routing via Spring Cloud Gateway
├── hotel-service/             # Hotel and room management microservice
├── reservation-service/       # Reservation handling microservice
├── notification-service/      # Kafka consumer for reservation events
├── common/                    # Shared DTOs and constants across services
├── docker-compose.yml         # Service orchestration (PostgreSQL, Kafka, Zookeeper, etc.)
└── init/                      # Initial DB creation scripts
```

---

## ⚙️ Tech Stack

- **Java 17**
- **Spring Boot 3.5.3**
- **Spring Data JPA**
- **Spring Validation**
- **Spring Cloud Gateway**
- **Kafka + Spring Kafka**
- **PostgreSQL**
- **Docker & Docker Compose**
- **Lombok**
- **JUnit & Mockito**

---

## 🚀 Getting Started

### Maven

Run maven clean install

### 🐳 Run with Docker

Make sure Docker is installed and running.

```bash
docker-compose up --build
```

This will spin up:
- PostgreSQL
- Zookeeper + Kafka
- All microservices
- API Gateway at `http://localhost:9080`


## 📂 Database Initialization

Databases (`hotel_db`, `reservation_db`) are created using `init/init.sql`.

Dummy data is inserted via each service's `data.sql` file (Spring Boot auto-executes it).

You can verify with:

```sql
SELECT * FROM hotel;
SELECT * FROM room;
```

---

## 🧪 Testing

### ✅ Unit Tests

All services include unit tests using JUnit and Mockito.

Example: `HotelServiceImplTest.java`

```bash
./mvnw test
```

### 🔁 Integration Tests

- Written using `@SpringBootTest` and `TestRestTemplate`
- Target real PostgreSQL inside Docker

Example: `HotelControllerIntegrationTest.java`

---

## 🔐 Authentication

- Dummy login is provided via `/auth/login` endpoint in API Gateway.
- JWT token is issued if username/password matches predefined users:
    - `admin/admin`(ROLE_ADMIN)
    - `user1/1234`(ROLE_USER)
    - `user2/1234`(ROLE_USER)
    - `user2/1234`(ROLE_USER)
    - `user4/1234`(ROLE_USER)

Use the token for all protected routes via:

```
Authorization: Bearer <token>
```

---

## 🧪 API Testing with Postman

### 📥 Import Postman Collection

1. Open Postman
2. Click **Import**
3. Paste the collection JSON provided separately or use the `HotelMicroservicesAPI.postman_collection.json` file if included in the repo.

> Sample login, hotel/room/reservation CRUD, and user-specific endpoints included.

---

## ✉️ Kafka Integration

- `reservation-service` produces events to Kafka (`reservation-created` topic)
- `notification-service` consumes and logs reservation events

Make sure Kafka is running at `kafka:9092`.

---

## 🧪 Sample Login Request

```http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin"
}
