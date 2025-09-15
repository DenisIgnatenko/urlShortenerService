# URL Shortener Service

This service provides functionality to shorten long URLs into compact, unique hashes for easier sharing and tracking. It supports fast redirection, caching, resilience mechanisms, and is designed with scalability and performance in mind.

## Core Features

- Shorten long URLs and return unique shortened hashes.
- Redirect to the original URL when shortened link is accessed.
- Deduplication: prevents duplicate entries.
- In-memory caching for fast lookups.
- Retry logic for transient failures.
- Thread pool for async task handling.
- Logging and error tracking.

## Technology Stack

- Java 17+
- Spring Boot
- PostgreSQL
- Local in-memory cache (optional Redis)
- Gradle
- OpenAPI (Swagger)
- JUnit 5 + Mockito

## Architecture

- `URLShortenerController`: REST API handler.
- `URLShortenerService`: Business logic.
- `HashGenerator`: Base62 hash generation.
- `LocalCache`: TTL-enabled cache.
- `RetryableServiceWrapper`: Retry logic wrapper.
- `ThreadPoolConfig`: Thread pool executor config.
- `ShortenedUrl`: JPA Entity.
- `URLRepository`: URL database access.
- `UniqueIDRepository`: Unique ID generator.

## API Overview

### POST /api/shorten

Request:
```json
{
  "url": "https://example.com/very/long/path"
}
```

Response:
```json
{
  "shortUrl": "http://localhost:8080/a3kD2c"
}
```

### GET /{hash}

Redirects to the original long URL.

## Design Notes

- Base62 encoding of unique numeric ID ensures URL safety and compactness.
- Identical long URLs return the same shortened version.
- Resilient with retry and backoff logic.
- Thread-safe with concurrent structures and executor bounds.
- Unit-tested core logic.

## Future Improvements

- Expiring URLs after TTL.
- Metadata storage (timestamp, IP, user-agent).
- Distributed caching (Redis).
- Rate limiting and API keys.
- Click analytics.
- Monitoring (Prometheus, Grafana).

## How to Run

1. Clone the repo.
2. Configure PostgreSQL in `application.yml`.
3. Run the application:
```bash
./gradlew bootRun
```
4. Swagger available at: `http://localhost:8080/swagger-ui/index.html`

## Author

Denis Ignatenko  
Backend Engineer | Java, Spring Boot, System Design
