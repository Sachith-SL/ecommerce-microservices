# Ecommerce Microservices

## 1. Architecture overview

This project follows a microservices architecture with independent services for product, inventory, and order management.

- Each service has its own Spring Boot application and its own PostgreSQL database.
- Databases are provided via Docker Compose.
- Services run independently on different HTTP ports.

High-level runtime layout:

- `product-service` -> `product-db`
- `inventory-service` -> `inventory-db`
- `order-service` -> `order-db`

## 2. Microservices list

1. `product-service`
	- HTTP port: `8081`
	- DB: `productdb` on `localhost:5436`

2. `inventory-service`
	- HTTP port: `8082`
	- DB: `inventorydb` on `localhost:5434`

3. `order-service`
	- HTTP port: `8083`
	- DB: `orderdb` on `localhost:5435`

## 3. How to start system

### Prerequisites

- Java `21`
- Docker + Docker Compose

### Step 1: Start databases

From project root:

```bash
docker-compose up -d
```

### Step 2: Start services

Open separate terminals and run:

```bash
cd services/product-service
./gradlew bootRun
```

```bash
cd services/inventory-service
./gradlew bootRun
```

```bash
cd services/order-service
./gradlew bootRun
```

### Step 3: Verify health

- `http://localhost:8081/actuator/health`
- `http://localhost:8082/actuator/health`
- `http://localhost:8083/actuator/health`

## 4. API documentation

Current built-in endpoints available from Spring Actuator:

- `GET /actuator/health`
- `GET /actuator/info`

Service base URLs:

- Product: `http://localhost:8081`
- Inventory: `http://localhost:8082`
- Order: `http://localhost:8083`

If you later add Swagger/OpenAPI, expose docs at `/swagger-ui.html` or `/v3/api-docs` for each service.

## 5. Saga flow explanation

Typical saga flow for order processing in this architecture:

1. Order is created in `order-service` (status: `PENDING`).
2. `order-service` requests stock reservation from `inventory-service`.
3. If stock is available, `inventory-service` confirms reservation.
4. `order-service` marks order as `CONFIRMED`.
5. If reservation fails, compensation runs and order is marked `CANCELLED`.

Note: This is the conceptual saga flow. Implementations can be orchestration-based or choreography-based depending on your event/command design.

## 6. Technology stack

- Java: `21`
- Spring Boot: `4.0.3`
- Spring Framework: `7.x` (managed by Spring Boot)
- Build tool: Gradle `8.14`
- Database: PostgreSQL `15` (Docker containers)
- ORM: Spring Data JPA + Hibernate
- Monitoring: Spring Boot Actuator
