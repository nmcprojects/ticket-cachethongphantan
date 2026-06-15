# 03 — Event Catalog

> **Scope**: All Kafka events with JSON schemas, producers, and consumers  
> **Owner**: Inventory & Notification Service Team  
> **Date**: 2026-06-14  
> **Status**: Design Document

---

## 1. Overview

This document defines all events that flow through the Kafka message bus. Each event includes:
- **Schema**: JSON structure with field descriptions
- **Producer**: Which service publishes the event
- **Consumer**: Which service(s) consume the event
- **Topic**: Which Kafka topic the event is published to
- **Partition Key**: Which field determines the partition

---

## 2. Event Naming Convention

```
{Domain}{Action}{Status}

Examples:
  BookingCreated
  PaymentSucceeded
  SeatsHeld
  SeatsConfirmed
  InventoryLow
  TicketIssued
  EmailSent
  NotificationFailed
```

---

## 3. Event Base Schema

Every event extends this base schema:

```json
{
  "eventId": "uuid-v4-string",
  "eventType": "BookingCreated",
  "aggregateType": "Booking",
  "aggregateId": "12345",
  "sourceService": "bookingService",
  "timestamp": "2026-06-14T10:30:00Z",
  "correlationId": "uuid-v4-string",
  "payload": { ... }
}
```

| Field | Type | Description |
|-------|------|-------------|
| `eventId` | UUID | Unique identifier for this event occurrence |
| `eventType` | String | Event type (e.g., `BookingCreated`) |
| `aggregateType` | String | Domain aggregate (e.g., `Booking`, `Payment`) |
| `aggregateId` | String | ID of the aggregate instance |
| `sourceService` | String | Service that produced the event |
| `timestamp` | ISO-8601 | When the event occurred |
| `correlationId` | UUID | Traces the request across services |
| `payload` | Object | Event-specific data |

---

## 4. Inventory Service Events (Producer)

### 4.1 SeatsHeld

**Published when**: User selects seats and booking is created

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440000",
  "eventType": "SeatsHeld",
  "aggregateType": "Inventory",
  "aggregateId": "event-123",
  "sourceService": "inventoryService",
  "timestamp": "2026-06-14T10:30:00Z",
  "correlationId": "corr-abc-123",
  "payload": {
    "eventId": 123,
    "bookingId": 456,
    "userId": 789,
    "ticketTypeId": 101,
    "ticketTypeName": "VIP Standard",
    "quantity": 2,
    "holdExpiration": "2026-06-14T10:40:00Z",
    "seats": [
      { "seatId": "A-1", "seatNumber": "A1" },
      { "seatId": "A-2", "seatNumber": "A2" }
    ],
    "availableAfterHold": 48,
    "reservedAfterHold": 2
  }
}
```

| Field | Type | Description |
|-------|------|-------------|
| `eventId` | Long | Event being booked |
| `bookingId` | Long | Booking reference |
| `userId` | Long | User who made the booking |
| `ticketTypeId` | Long | Type of ticket |
| `quantity` | Integer | Number of seats held |
| `holdExpiration` | ISO-8601 | When hold expires (TTL) |
| `seats` | Array | Specific seat identifiers |
| `availableAfterHold` | Integer | Remaining available seats |
| `reservedAfterHold` | Integer | Total reserved seats |

**Topic**: `inventory.updates`
**Partition Key**: `eventId`
**Consumers**: `bookingService`, `analyticsService`

---

### 4.2 SeatsConfirmed

**Published when**: Payment succeeds

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440001",
  "eventType": "SeatsConfirmed",
  "aggregateType": "Inventory",
  "aggregateId": "event-123",
  "sourceService": "inventoryService",
  "timestamp": "2026-06-14T10:35:00Z",
  "correlationId": "corr-abc-123",
  "payload": {
    "eventId": 123,
    "bookingId": 456,
    "userId": 789,
    "ticketTypeId": 101,
    "quantity": 2,
    "seats": [
      { "seatId": "A-1", "seatNumber": "A1" },
      { "seatId": "A-2", "seatNumber": "A2" }
    ],
    "availableAfterConfirm": 48,
    "soldAfterConfirm": 2,
    "ticketIds": [1001, 1002]
  }
}
```

| Field | Type | Description |
|-------|------|-------------|
| `ticketIds` | Array<Long> | IDs of created tickets |
| `soldAfterConfirm` | Integer | Total sold after this confirmation |

**Topic**: `inventory.updates`
**Partition Key**: `eventId`
**Consumers**: `bookingService`, `ticketService`, `analyticsService`

---

### 4.3 SeatsReleased

**Published when**: Hold expires or payment fails

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440002",
  "eventType": "SeatsReleased",
  "aggregateType": "Inventory",
  "aggregateId": "event-123",
  "sourceService": "inventoryService",
  "timestamp": "2026-06-14T10:40:00Z",
  "correlationId": "corr-abc-123",
  "payload": {
    "eventId": 123,
    "bookingId": 456,
    "userId": 789,
    "ticketTypeId": 101,
    "quantity": 2,
    "seats": [
      { "seatId": "A-1", "seatNumber": "A1" },
      { "seatId": "A-2", "seatNumber": "A2" }
    ],
    "reason": "PAYMENT_FAILED",
    "availableAfterRelease": 50,
    "reservedAfterRelease": 0
  }
}
```

| Field | Type | Description |
|-------|------|-------------|
| `reason` | Enum | `PAYMENT_FAILED`, `BOOKING_EXPIRED`, `USER_CANCELLED` |

**Topic**: `inventory.updates`
**Partition Key**: `eventId`
**Consumers**: `bookingService`, `analyticsService`

---

### 4.4 InventoryLow

**Published when**: Available seats drop below threshold

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440003",
  "eventType": "InventoryLow",
  "aggregateType": "Inventory",
  "aggregateId": "event-123",
  "sourceService": "inventoryService",
  "timestamp": "2026-06-14T10:35:00Z",
  "correlationId": "corr-abc-123",
  "payload": {
    "eventId": 123,
    "ticketTypeId": 101,
    "ticketTypeName": "VIP Standard",
    "availableQuantity": 5,
    "totalQuantity": 100,
    "threshold": 10,
    "percentageRemaining": 5.0
  }
}
```

**Topic**: `inventory.updates`
**Partition Key**: `eventId`
**Consumers**: `eventService`, `analyticsService`

---

## 5. Notification Service Events (Producer)

### 5.1 EmailSent

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440004",
  "eventType": "EmailSent",
  "aggregateType": "Notification",
  "aggregateId": "notif-456",
  "sourceService": "notificationService",
  "timestamp": "2026-06-14T10:36:00Z",
  "correlationId": "corr-abc-123",
  "payload": {
    "notificationId": 456,
    "bookingId": 789,
    "userId": 123,
    "channel": "EMAIL",
    "recipient": "user@example.com",
    "template": "booking-confirmation",
    "providerMessageId": "sendgrid-msg-123",
    "sentAt": "2026-06-14T10:36:00Z"
  }
}
```

**Topic**: `notification.requests`
**Partition Key**: `userId`
**Consumers**: `analyticsService`

---

### 5.2 NotificationFailed

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440005",
  "eventType": "NotificationFailed",
  "aggregateType": "Notification",
  "aggregateId": "notif-456",
  "sourceService": "notificationService",
  "timestamp": "2026-06-14T10:36:00Z",
  "correlationId": "corr-abc-123",
  "payload": {
    "notificationId": 456,
    "bookingId": 789,
    "userId": 123,
    "channel": "EMAIL",
    "recipient": "user@example.com",
    "template": "booking-confirmation",
    "errorMessage": "SMTP connection timeout",
    "retryCount": 3,
    "willRetry": false
  }
}
```

**Topic**: `notification.dlq`
**Partition Key**: `userId`
**Consumers**: `monitoringService`, `opsTeam`

---

## 6. Events We Consume (from Other Teams)

### 6.1 BookingCreated

**Producer**: `bookingService`
**Topic**: `booking.lifecycle`

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440006",
  "eventType": "BookingCreated",
  "aggregateType": "Booking",
  "aggregateId": "456",
  "sourceService": "bookingService",
  "timestamp": "2026-06-14T10:30:00Z",
  "correlationId": "corr-abc-123",
  "payload": {
    "bookingId": 456,
    "userId": 789,
    "eventId": 123,
    "eventTitle": "Summer Music Festival",
    "customerEmail": "user@example.com",
    "totalAmount": 200.00,
    "currency": "USD",
    "items": [
      {
        "ticketTypeId": 101,
        "ticketTypeName": "VIP Standard",
        "quantity": 2,
        "unitPrice": 100.00,
        "totalPrice": 200.00
      }
    ],
    "expiredAt": "2026-06-14T10:40:00Z"
  }
}
```

**Consumers**: `inventoryService` (hold seats), `notificationService` (send confirmation)

---

### 6.2 PaymentSucceeded

**Producer**: `paymentService`
**Topic**: `payment.events`

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440007",
  "eventType": "PaymentSucceeded",
  "aggregateType": "Payment",
  "aggregateId": "pay-789",
  "sourceService": "paymentService",
  "timestamp": "2026-06-14T10:35:00Z",
  "correlationId": "corr-abc-123",
  "payload": {
    "paymentId": 789,
    "bookingId": 456,
    "userId": 123,
    "amount": 200.00,
    "currency": "USD",
    "provider": "STRIPE",
    "providerPaymentId": "pi_123456789",
    "paidAt": "2026-06-14T10:35:00Z"
  }
}
```

**Consumers**: `inventoryService` (confirm seats), `bookingService` (mark paid)

---

### 6.3 PaymentFailed

**Producer**: `paymentService`
**Topic**: `payment.events`

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440008",
  "eventType": "PaymentFailed",
  "aggregateType": "Payment",
  "aggregateId": "pay-789",
  "sourceService": "paymentService",
  "timestamp": "2026-06-14T10:35:00Z",
  "correlationId": "corr-abc-123",
  "payload": {
    "paymentId": 789,
    "bookingId": 456,
    "userId": 123,
    "amount": 200.00,
    "currency": "USD",
    "provider": "STRIPE",
    "failureReason": "CARD_DECLINED",
    "errorMessage": "Your card was declined.",
    "failedAt": "2026-06-14T10:35:00Z"
  }
}
```

**Consumers**: `inventoryService` (release seats), `bookingService` (mark failed), `notificationService` (send failure notice)

---

### 6.4 BookingExpired

**Producer**: `bookingService`
**Topic**: `booking.lifecycle`

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440009",
  "eventType": "BookingExpired",
  "aggregateType": "Booking",
  "aggregateId": "456",
  "sourceService": "bookingService",
  "timestamp": "2026-06-14T10:40:00Z",
  "correlationId": "corr-abc-123",
  "payload": {
    "bookingId": 456,
    "userId": 789,
    "eventId": 123,
    "expiredAt": "2026-06-14T10:40:00Z"
  }
}
```

**Consumers**: `inventoryService` (release seats), `notificationService` (send expiry notice)

---

### 6.5 TicketIssued

**Producer**: `inventoryService`
**Topic**: `ticket.lifecycle`

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440010",
  "eventType": "TicketIssued",
  "aggregateType": "Ticket",
  "aggregateId": "ticket-1001",
  "sourceService": "inventoryService",
  "timestamp": "2026-06-14T10:35:30Z",
  "correlationId": "corr-abc-123",
  "payload": {
    "ticketId": 1001,
    "bookingId": 456,
    "bookingItemId": 2001,
    "userId": 789,
    "customerEmail": "user@example.com",
    "eventId": 123,
    "eventTitle": "Summer Music Festival",
    "ticketTypeId": 101,
    "ticketTypeName": "VIP Standard",
    "ticketCode": "SMF-2026-A1-1001",
    "qrPayload": "encrypted-qr-data",
    "issuedAt": "2026-06-14T10:35:30Z"
  }
}
```

**Consumers**: `notificationService` (send ticket email)

---

### 6.6 TicketTypeUpdated

**Producer**: `eventService`
**Topic**: `event.catalog`

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440011",
  "eventType": "TicketTypeUpdated",
  "aggregateType": "TicketType",
  "aggregateId": "101",
  "sourceService": "eventService",
  "timestamp": "2026-06-14T09:00:00Z",
  "correlationId": "corr-xyz-456",
  "payload": {
    "ticketTypeId": 101,
    "eventId": 123,
    "name": "VIP Standard",
    "description": "Front row seats",
    "price": 100.00,
    "currency": "USD",
    "totalQuantity": 100,
    "availableQuantity": 50,
    "maxPerOrder": 4,
    "status": "SELLING",
    "updatedAt": "2026-06-14T09:00:00Z"
  }
}
```

**Consumers**: `inventoryService` (update `TicketTypeRead`)

---

## 7. Event Versioning Strategy

```
Event Type Naming: {Domain}{Action}{Status}

Versioning:
  - Add new fields only (backward compatible)
  - Never remove fields
  - Use "optional" for new fields
  - If breaking change needed, create new event type:
    BookingCreatedV2

Schema Evolution:
  - JSON schema stored in docs/schemas/
  - Consumer ignores unknown fields
  - Producer uses default values for missing fields
```

---

## 8. Event to Topic Mapping

| Event | Topic | Producer | Consumers | Partition Key |
|-------|-------|----------|-----------|---------------|
| `BookingCreated` | `booking.lifecycle` | `bookingService` | `inventoryService`, `notificationService` | `bookingId` |
| `BookingExpired` | `booking.lifecycle` | `bookingService` | `inventoryService`, `notificationService` | `bookingId` |
| `PaymentSucceeded` | `payment.events` | `paymentService` | `inventoryService`, `bookingService` | `paymentId` |
| `PaymentFailed` | `payment.events` | `paymentService` | `inventoryService`, `bookingService`, `notificationService` | `paymentId` |
| `SeatsHeld` | `inventory.updates` | `inventoryService` | `bookingService`, `analyticsService` | `eventId` |
| `SeatsConfirmed` | `inventory.updates` | `inventoryService` | `bookingService`, `ticketService`, `analyticsService` | `eventId` |
| `SeatsReleased` | `inventory.updates` | `inventoryService` | `bookingService`, `analyticsService` | `eventId` |
| `InventoryLow` | `inventory.updates` | `inventoryService` | `eventService`, `analyticsService` | `eventId` |
| `TicketIssued` | `ticket.lifecycle` | `inventoryService` | `notificationService`, `analyticsService` | `ticketId` |
| `TicketCheckedIn` | `ticket.lifecycle` | `inventoryService` | `analyticsService` | `ticketId` |
| `EmailSent` | `notification.requests` | `notificationService` | `analyticsService` | `userId` |
| `NotificationFailed` | `notification.dlq` | `notificationService` | `monitoringService` | `userId` |
| `TicketTypeUpdated` | `event.catalog` | `eventService` | `inventoryService` | `ticketTypeId` |

---

## 9. Event Processing Order (Saga Flow)

```
Step 1: BookingCreated (bookingService)
  ↓
Step 2: SeatsHeld (inventoryService) ← Business rule: hold seats
  ↓
Step 3: PaymentSucceeded (paymentService)
  ↓
Step 4: SeatsConfirmed (inventoryService) ← Business rule: confirm + create tickets
  ↓
Step 5: TicketIssued (inventoryService)
  ↓
Step 6: EmailSent (notificationService) ← Business rule: send ticket email
```

---

## 10. Correlation ID Tracking

All events in the same saga share the same `correlationId`:

```
User Request: correlationId = "corr-abc-123"

┌─ BookingCreated     ─ correlationId: corr-abc-123
├─ SeatsHeld          ─ correlationId: corr-abc-123
├─ PaymentSucceeded   ─ correlationId: corr-abc-123
├─ SeatsConfirmed     ─ correlationId: corr-abc-123
├─ TicketIssued       ─ correlationId: corr-abc-123
└─ EmailSent          ─ correlationId: corr-abc-123
```

This enables distributed tracing across all services.

---

## 11. Event Audit Trail

Both services store all consumed events in their respective Inbox tables:

```sql
-- inventoryService
inventory_inbox_event:
  - id
  - source_service
  - event_id (UUID, unique constraint)
  - event_type
  - payload (JSON)
  - status: RECEIVED | PROCESSED | FAILED
  - received_at
  - processed_at
  - error_message

-- notificationService
notification_inbox_event:
  - id
  - source_service
  - event_id (UUID, unique constraint)
  - event_type
  - payload (JSON)
  - status: RECEIVED | PROCESSED | FAILED
  - received_at
  - processed_at
  - error_message
```

---

## 12. Next Steps

1. Define Java event classes (POJOs) in `com.ticketbooking.inventory.event` package
2. Implement Kafka producers with `InventoryOutboxEvent` relay
3. Implement Kafka consumers with idempotent Inbox processing
4. Add `eventId` unique constraints to Inbox tables
5. Configure distributed tracing (Zipkin) with `correlationId`
