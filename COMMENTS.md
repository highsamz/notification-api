# Architectural Decisions – Notification API

This document describes the key architectural decisions made in the **notification-api** service.

---

# 1. Separation Between HTTP Ingestion and Processing

The system was designed with **two independent services**:

* `notification-api`
* `subscription-processor`

The main reason for this separation is **decoupling HTTP ingestion from domain processing**.

Benefits:

* Improved scalability
* Better fault isolation
* Asynchronous processing
* Independent deployment and scaling

The API simply publishes events and delegates all business logic to the processor service.

---

# 2. Asynchronous Event Processing

RabbitMQ was introduced as the communication mechanism between services.

Advantages:

* Loose coupling between services
* Resilience to temporary processor failures
* Better throughput under load
* Ability to scale consumers independently

The API publishes events to the queue:

```
subscription.notification.queue
```

---

# 3. Minimal Business Logic in the API

The API intentionally contains **no business logic**.

Responsibilities are limited to:

* Input validation
* Event publication

All domain logic is implemented in the `subscription-processor`.

This follows the **single responsibility principle** and keeps the API lightweight.

---

# 4. Use of DTOs

Two DTO types were created:

### Request DTO

Represents the **HTTP contract**.

Example:

```
NotificationRequest
```

### Message DTO

Represents the **event contract sent to RabbitMQ**.

Example:

```
NotificationEventMessage
```

This separation prevents coupling between HTTP contracts and messaging contracts.

---

# 5. Enum Validation at the API Boundary

The `eventType` field is validated using an enum.

This guarantees that only supported events enter the system.

Example values:

* SUBSCRIPTION_PURCHASED
* SUBSCRIPTION_CANCELED
* SUBSCRIPTION_RESTARTED

This prevents invalid events from being propagated to the messaging layer.

---

# 6. Error Handling Strategy

The API includes a global exception handler to ensure consistent error responses.

Handled cases include:

* Invalid request payload
* Malformed JSON
* RabbitMQ publishing failures
* Unexpected errors

Publishing failures are converted to a `503 Service Unavailable` response.

---

# 7. Asynchronous Response Model

The API returns:

```
202 Accepted
```

instead of `200 OK`.

This indicates that the request has been accepted but processing will occur asynchronously.

This design aligns with **event-driven architecture principles**.

---

# 8. Test Coverage Strategy

Tests were implemented to cover the most relevant scenarios:

### Controller tests

* Valid request returns 202
* Invalid payload returns 400
* Invalid event type returns 400

### Messaging tests

* Publisher sends message to RabbitMQ
* Correct payload transformation

### Error tests

* Publishing failure returns 503
* Malformed JSON returns 400

The goal of these tests is to validate **API behavior and integration boundaries** rather than internal implementation details.

---

# 9. Design Principles Applied

Key engineering principles applied in this service:

* Separation of concerns
* Event-driven architecture
* Input validation at system boundaries
* Minimal API responsibilities
* Asynchronous processing
* Testable components

These principles improve **maintainability, scalability, and resilience** of the system.
