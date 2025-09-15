# 🔗 URL Shortener Microservice

A production-ready, high-performance URL Shortening Service built with **Java 17**, **Spring Boot**, **PostgreSQL**, **Redis**, and **Docker**.  
Designed using **Clean Architecture**, **SOLID principles**, and **scalable practices** to handle real-world workloads.

---

## 🧠 Overview

This service allows users to generate short, unique URLs from long links and redirect users back to the original destination. It ensures:

- Fast access through in-memory caching
- Persistent storage and recovery via PostgreSQL
- High availability and resilience using retry policies and fault-tolerant design
- Asynchronous processing using thread pools
- Clean, modular code following DDD and SRP

---

## 🧱 Architecture

```
                +---------------------+
                |    REST Controller  |
                +---------------------+
                          |
                          v
                +---------------------+
                |     URL Service     |
                +---------------------+
                | - Generate Hash     |
                | - Save to DB/Cache  |
                | - Redirect Logic    |
                +---------------------+
                          |
         +----------------+------------------+
         |                                   |
         v                                   v
+--------------------+           +----------------------+
|   Redis Cache      |           |   PostgreSQL DB      |
|  (LocalCache impl) |           |  (JPA Repositories)  |
+--------------------+           +----------------------+
```

---

## ⚙️ Technologies Used

| Layer            | Stack                                     |
|------------------|-------------------------------------------|
| Language         | Java 17                                   |
| Framework        | Spring Boot 3.x                           |
| Database         | PostgreSQL (JPA/Hibernate)                |
| Cache            | Redis (custom LocalCache as fallback)     |
| API Docs         | OpenAPI (Swagger UI via Springdoc)        |
| Containerization | Docker + Docker Compose                   |
| Build Tool       | Gradle                                    |
| Config Mgmt      | Spring `@ConfigurationProperties`         |
| Retry Policy     | `@Retryable` + `@Recover` + backoff logic |
| Logging          | SLF4J + Logback                           |
| Validation       | Jakarta Bean Validation (JSR-380)         |
| Testing          | JUnit 5, Mockito, Testcontainers          |

---

## 🚀 Features

- ✅ Shorten URLs with unique 6-character keys
- ✅ In-memory cache for fast lookups
- ✅ REST API with Swagger documentation
- ✅ Retry on DB failures
- ✅ Idempotency and conflict prevention
- ✅ URL expiration handling (optional)
- ✅ Asynchronous key generation
- ✅ Fully dockerized
- ✅ CI-ready with test coverage

---

## 🧪 API Endpoints

| Method | Endpoint           | Description               |
|--------|--------------------|---------------------------|
| POST   | `/api/v1/shorten`  | Generate shortened URL    |
| GET    | `/r/{shortCode}`   | Redirect to full URL      |

View full API docs at: `http://localhost:8080/swagger-ui.html`

---

## 🐳 Run with Docker

```bash
docker-compose up --build
```

This spins up:

- `url-shortener-app` (Spring Boot)
- `postgres` (DB)
- `redis` (cache)

---

## 🧪 Running Tests

```bash
./gradlew test
```

Includes unit and integration tests for:

- URL shortening flow
- Cache behavior
- Hash collision detection
- Service layer logic

---

## 📁 Project Structure

```
src/
 └── main/
      ├── controller/
      ├── service/
      ├── entity/
      ├── repository/
      ├── cache/
      ├── config/
      └── exception/
```

---

## 🔐 Environment Configuration

Application properties use `application.yml`. Customize with:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/url_shortener
    username: postgres
    password: postgres
  redis:
    host: localhost
    port: 6379

retry:
  maxAttempts: 3
  backoff: 2000
```

---

## 🧰 Roadmap (Optional Extensions)

- 🔒 Authenticated shortening
- 📈 Analytics per link (clicks, devices, geos)
- 🗑 Expiring links
- 📬 Email/SMS notifications
- 🌐 Custom aliases (`denis.link/awesome`)

---

## 🧑‍💻 Author

**Denis Ignatenko**  
Backend & Fullstack Engineer  
[LinkedIn](https://www.linkedin.com/in/denis-ignatenko/) • [GitHub](https://github.com/...)

---

## 📄 License

MIT License
