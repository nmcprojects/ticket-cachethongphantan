# 01 — System Architecture

> **Scope**: Ticket Booking System — Microservices Architecture  
> **Owner**: Inventory & Notification Service Team  
> **Date**: 2026-06-14  
> **Status**: Design Document

---

## 1. Overview

This document describes the system architecture for the **Ticket Booking System**, focusing on the services owned by our team:

- **inventoryService** (renamed from `ticketService`, port 9005)
- **notificationService** (port 9006)

We also own and operate the **Kafka message bus** infrastructure that enables event-driven communication across all microservices.

---

## 2. Architecture Principles

| Principle | Description |
|-----------|-------------|
| **Database-per-Service** | Each microservice owns its own PostgreSQL database. No shared tables. |
| **Event-Driven Communication** | Services communicate via Kafka topics. No direct REST calls between services for business events. |
| **Zero Impact on Other Teams** | We do not modify other teams' services, databases, or configurations. All integration is via Kafka. |
| **Strong Consistency for Inventory** | Seat inventory uses optimistic locking + Redis TTL holds to prevent overselling. |
| **At-Least-Once Delivery** | Kafka + Outbox pattern ensures no events are lost. |
| **CQRS for Inventory Read Model** | `inventoryService` maintains its own read-only copy of `TicketType` data, synced via Kafka. |

---

## 3. Service Topology

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              CLIENT (React)                              │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                           GATEWAY (port 9000)                            │
│                     (Routing, JWT Auth, Rate Limiting)                   │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
         ┌──────────────────────────┼──────────────────────────┐
         │                          │                          │
         ▼                          ▼                          ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  eventService   │    │  bookingService │    │  paymentService │
│   (port 9002)   │    │   (port 9003)   │    │   (port 9004)   │
│    MySQL        │    │     MySQL       │    │     MySQL       │
│  Other Teams    │    │   Other Teams   │    │   Other Teams   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                          │                          │
         │          ┌───────────────┴───────────────┐           │
         │          │                               │           │
         ▼          ▼                               ▼           ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         KAFKA (message bus)                              │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐             │
│  │ booking.lifecycle│  │ payment.events  │  │ inventory.updates│            │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘             │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐             │
│  │ ticket.lifecycle│  │ notification.req│  │ notification.dlq │            │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘             │
└─────────────────────────────────────────────────────────────────────────┘
         │                          │
         ▼                          ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                      OUR SERVICES (PostgreSQL)                           │
│  ┌─────────────────────────────┐  ┌─────────────────────────────────────┐  │
│  │   inventoryService          │  │      notificationService            │  │
│  │   (port 9005)               │  │      (port 9006)                    │  │
│  │                             │  │                                     │  │
│  │  Entities:                  │  │  Entities:                          │  │
│  │  • Ticket (existing)        │  │  • EmailNotification (existing)     │  │
│  │  • CheckinLog (existing)    │  │  • EmailNotificationItem (existing) │  │
│  │  • SeatHold (NEW)           │  │  • NotificationInboxEvent (existing)│  │
│  │  • TicketTypeRead (NEW)     │  │  • NotificationRequest (NEW)      │  │
│  │  • InventoryOutboxEvent     │  │  • NotificationDelivery (NEW)     │  │
│  │  • InventoryInboxEvent (NEW)│  │  • NotificationTemplate (NEW)     │  │
│  │                             │  │  • NotificationOutboxEvent (NEW)  │  │
│  │  Redis: seat holds          │  │                                     │  │
│  └─────────────────────────────┘  └─────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 4. Service Responsibilities

### 4.1 inventoryService (port 9005)

| Capability | Description |
|------------|-------------|
| **Seat Inventory Management** | Holds, confirms, and releases seat reservations with strong consistency |
| **Ticket Issuance** | Creates `Ticket` entities after payment confirmation (existing) |
| **Check-in** | Validates QR codes and logs `CheckinLog` entries (existing) |
| **Event Publication** | Publishes `inventory.updates` events via Kafka Outbox |
| **Event Consumption** | Consumes `booking.lifecycle`, `payment.events` to update inventory state |
| **Read Model Sync** | Maintains `TicketTypeRead` table synced from `eventService` |

### 4.2 notificationService (port 9006)

| Capability | Description |
|------------|-------------|
| **Multi-Channel Dispatch** | Email (Thymeleaf), SMS (Twilio), Push (FCM) |
| **Event-Driven Triggering** | Listens to `booking.lifecycle`, `payment.events`, `ticket.lifecycle` |
| **Template Management** | Thymeleaf HTML templates with plaintext fallback |
| **Idempotency** | Deduplicates via `eventId` unique constraint |
| **Retry & DLQ** | 3 retries with exponential backoff, then dead letter queue |
| **Event Publication** | Publishes delivery status via `notification.requests` |

---

## 5. Data Flow — Happy Path (Booking → Payment → Inventory → Notification)

```
User selects seats → Booking Service creates booking
                          │
                          ▼ (Kafka: booking.lifecycle / BookingCreated)
              ┌─────────────────────────────┐
              │    inventoryService           │
              │    1. Holds seats in Redis    │
              │    2. Publishes SeatsHeld     │
              └─────────────────────────────┘
                          │
                          ▼ (REST: user pays)
              ┌─────────────────────────────┐
              │    paymentService             │
              │    Processes payment          │
              └─────────────────────────────┘
                          │
                          ▼ (Kafka: payment.events / PaymentSucceeded)
              ┌─────────────────────────────┐
              │    inventoryService           │
              │    1. Confirms seats (DB)     │
              │    2. Publishes SeatsConfirmed│
              │    3. Creates Ticket entities │
              └─────────────────────────────┘
                          │
                          ▼ (Kafka: ticket.lifecycle / TicketIssued)
              ┌─────────────────────────────┐
              │    notificationService        │
              │    1. Sends email + SMS       │
              │    2. Publishes EmailSent     │
              └─────────────────────────────┘
```

---

## 6. Data Flow — Failure Path (Payment Failed)

```
User selects seats → Booking Service creates booking
                          │
                          ▼ (Kafka: booking.lifecycle / BookingCreated)
              ┌─────────────────────────────┐
              │    inventoryService           │
              │    Holds seats in Redis       │
              └─────────────────────────────┘
                          │
                          ▼ (REST: user pays)
              ┌─────────────────────────────┐
              │    paymentService             │
              │    Payment fails              │
              └─────────────────────────────┘
                          │
                          ▼ (Kafka: payment.events / PaymentFailed)
              ┌─────────────────────────────┐
              │    inventoryService           │
              │    1. Releases seats          │
              │    2. Publishes SeatsReleased   │
              └─────────────────────────────┘
                          │
                          ▼ (Kafka: notification.requests)
              ┌─────────────────────────────┐
              │    notificationService        │
              │    Sends payment-failed email   │
              └─────────────────────────────┘
```

---

## 7. Technology Stack

| Layer | Technology | Version |
|-------|------------|---------|
| **Framework** | Spring Boot | 3.4.5 |
| **Language** | Java | 17 |
| **Build** | Maven | 3.2.5 |
| **Database** | PostgreSQL | 16 |
| **ORM** | Hibernate / JPA | 6.x |
| **Migration** | Liquibase | 4.x |
| **Message Bus** | Apache Kafka | 3.7.x |
| **Coordination** | Zookeeper | 3.9.x |
| **Cache** | Redis | 7.x |
| **Service Discovery** | Eureka (JHipster Registry) | 7.5.0 |
| **Templates** | Thymeleaf | 3.1.x |
| **Serialization** | Jackson | 2.17.x |
| **Monitoring** | Prometheus + Grafana | — |
| **Tracing** | Zipkin | — |

---

## 8. Database Schema (Our Services)

### 8.1 inventoryService PostgreSQL Schema

```sql
-- Existing entities (from JHipster JDL)
ticket              -- Electronic tickets
checkin_log         -- Check-in history
ticket_outbox_event -- Outbox pattern (existing, renamed topic)
ticket_inbox_event  -- Inbox pattern (existing, renamed topic)

-- NEW entities
seat_hold           -- Redis seat hold audit trail (optional persistent backup)
ticket_type_read    -- Read-only copy of eventService.ticket_type
inventory_outbox_event -- NEW outbox table for inventory events
inventory_inbox_event  -- NEW inbox table for consumed events
```

### 8.2 notificationService PostgreSQL Schema

```sql
-- Existing entities (from JHipster JDL)
email_notification      -- Email records
email_notification_item -- Per-ticket email items
notification_inbox_event -- Inbox events

-- NEW entities
notification_request    -- Unified multi-channel notification
notification_delivery   -- Per-channel delivery attempt
notification_template   -- Thymeleaf template registry
notification_outbox_event -- Outbox for delivery status events
```

---

## 9. Integration Boundaries

### 9.1 What We Consume (from other teams)

| Source Service | Topic | Event | Action |
|----------------|-------|-------|--------|
| bookingService | `booking.lifecycle` | `BookingCreated` | Hold seats in Redis |
| bookingService | `booking.lifecycle` | `BookingExpired` | Release seats |
| paymentService | `payment.events` | `PaymentSucceeded` | Confirm seats, create tickets |
| paymentService | `payment.events` | `PaymentFailed` | Release seats |
| eventService | `event.catalog` | `TicketTypeUpdated` | Update `TicketTypeRead` |
| ticketService | `ticket.lifecycle` | `TicketIssued` | Trigger notification |

### 9.2 What We Produce (for other teams)

| Topic | Event | Consumer |
|-------|-------|----------|
| `inventory.updates` | `SeatsHeld` | bookingService, analytics |
| `inventory.updates` | `SeatsConfirmed` | bookingService, analytics |
| `inventory.updates` | `SeatsReleased` | bookingService, analytics |
| `inventory.updates` | `InventoryLow` | eventService, analytics |
| `notification.requests` | `EmailSent` | analytics |
| `notification.requests` | `NotificationFailed` | monitoring, ops |
| `ticket.lifecycle` | `TicketIssued` | notificationService |
| `ticket.lifecycle` | `TicketCheckedIn` | analytics |

---

## 10. Key Design Decisions

| Decision | Rationale |
|----------|-----------|
| **PostgreSQL for our services** | Team preference. Other teams keep MySQL. No conflict. |
| **Rename ticketService → inventoryService** | Better reflects domain responsibility. Port stays 9005. |
| **Redis for seat holds** | Sub-10ms latency, TTL support, distributed atomic operations. |
| **Outbox pattern for Kafka** | Ensures atomicity: DB commit + Kafka publish in one transaction. |
| **CQRS read model for TicketType** | Avoids REST calls to eventService during high-concurrency booking. |
| **Thymeleaf for email** | Already bundled in JHipster. HTML + plaintext support. |
| **Manual Kafka integration** | Project already generated. Manual `spring-kafka` gives full control. |
| **Partition by eventId** | Ensures sequential processing of events for the same event. |

---

## 11. Security

- **JWT Authentication**: All services share the same Base64 JWT secret (configured in JHipster JDL)
- **Service-to-Service**: Kafka communication is internal to Docker network. No external access.
- **Database**: PostgreSQL credentials via environment variables. No hardcoded secrets.

---

## 12. Next Steps

1. Review `02-kafka-infrastructure.md` for Kafka setup
2. Review `03-event-catalog.md` for event schemas
3. Review `04-inventory-service-design.md` for seat logic
4. Review `05-notification-service-design.md` for multi-channel dispatch
5. Review `06-outbox-pattern-implementation.md` for reliability
6. Review `07-high-concurrency-strategy.md` for 10K+ users
7. Review `08-team-integration-guide.md` for team contracts
