# Subscription Notification System

## Overview

This project implements an **event-driven subscription processing system** using **Spring Boot**, **RabbitMQ**, and **PostgreSQL**.

The system is composed of two services:

* **notification-api** – receives subscription events through HTTP and publishes them asynchronously to RabbitMQ.
* **subscription-processor** – consumes events from RabbitMQ and applies business rules to persist subscription state and event history.

The architecture is designed to **decouple HTTP ingestion from business processing**, improving scalability and resilience.

---

## Architecture

The system follows an **event-driven architecture**.

```
Client
   │
   ▼
notification-api
   │
   │  (publish event)
   ▼
RabbitMQ
   │
   │  (consume event)
   ▼
subscription-processor
   │
   ▼
PostgreSQL
```

### Key Benefits

* Asynchronous processing
* Decoupled services
* Resilient event handling
* Scalable architecture
* Clear separation of responsibilities

---

## Project Structure

```
subscription-notification-system
│
├── docker-compose.yml
│
├── notification-api
│   ├── README.md
│   └── COMMENTS.md
│
└── subscription-processor
    ├── README.md
    └── COMMENTS.md
```

### Services

| Service                | Responsibility                                          |
| ---------------------- | ------------------------------------------------------- |
| notification-api       | Receives HTTP events and publishes messages to RabbitMQ |
| subscription-processor | Consumes events and updates subscription state          |

---

## Event Flow

1. Client sends an HTTP request to **notification-api**.
2. The request is validated and converted to a messaging payload.
3. The event is published to **RabbitMQ**.
4. **subscription-processor** consumes the event.
5. Business rules are applied.
6. Subscription state and event history are persisted in **PostgreSQL**.

---

## Events Supported

| Event                  | Description                          |
| ---------------------- | ------------------------------------ |
| SUBSCRIPTION_PURCHASED | Creates a new subscription           |
| SUBSCRIPTION_CANCELED  | Cancels an existing subscription     |
| SUBSCRIPTION_RESTARTED | Reactivates an existing subscription |

---

## Infrastructure

The project uses **Docker Compose** to run required infrastructure services.

Services included:

* PostgreSQL
* RabbitMQ (with management UI)

### Start infrastructure

```
docker compose up -d
```

RabbitMQ management console:

```
http://localhost:15672
```

Default credentials:

```
user: guest
password: guest
```

---

## Running the Applications

Each service can be started independently.

### Run notification-api

```
cd notification-api
./mvnw spring-boot:run
```

### Run subscription-processor

```
cd subscription-processor
./mvnw spring-boot:run
```

---

## Example Event

Request sent to `notification-api`:

```
POST /notifications
```

Payload:

```json
{
  "subscriptionId": "sub_123",
  "eventType": "SUBSCRIPTION_PURCHASED"
}
```

This event will be published to RabbitMQ and processed asynchronously by the `subscription-processor`.

---

## Querying Subscription State

The `subscription-processor` exposes endpoints to query subscription data.

### Get subscription

```
GET /subscriptions/{id}
```

### Get subscription history

```
GET /subscriptions/{id}/history?page=0&size=5&sort=processedAt,desc
```

---

## Technology Stack

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* RabbitMQ
* PostgreSQL
* Flyway
* MapStruct
* Lombok
* JUnit 5
* Mockito
* Docker

---

## Testing

The project includes unit tests covering:

* Business rules
* Event processing
* Controller validation
* Messaging behavior
* Error scenarios

Run tests with:

```
./mvnw test
```

---

## Design Principles

Key design principles applied in this project:

* Event-driven architecture
* Separation of concerns
* Layered architecture
* Clear domain boundaries
* Resilient message processing
* Comprehensive error handling
* Testable business logic

---

## Notes

Detailed architectural decisions and trade-offs can be found in:

```
notification-api/COMMENTS.md
subscription-processor/COMMENTS.md
```
