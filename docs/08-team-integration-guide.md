# 08 — Team Integration Guide

> **Scope**: How other teams integrate with Inventory & Notification services  
> **Owner**: Inventory & Notification Service Team  
> **Date**: 2026-06-14  
> **Status**: Design Document

---

## 1. Overview

This document serves as a **contract** for other teams. It specifies:

- What events **other teams must produce** for our services to work
- What events **we produce** that other teams can consume
- JSON schemas for all events
- Kafka topic mappings
- Error handling and retry behavior

---

## 2. Service Owners

| Service | Owner | Port | Database |
|---------|-------|------|----------|
| `gateway` | Frontend Team | 9000 | MySQL |
| `eventService` | Event Team | 9002 | MySQL |
| `bookingService` | Booking Team | 9003 | MySQL |
| `paymentService` | Payment Team | 9004 | MySQL |
| `inventoryService` | **Our Team** | 9005 | **PostgreSQL** |
| `notificationService` | **Our Team** | 9006 | **PostgreSQL** |

---

## 3. Events Other Teams Must Produce

### 3.1 For inventoryService

#### BookingCreated

**Producer**: `bookingService`  
**Topic**: `booking.lifecycle`  
**When to send**: After booking is created and user selects seats

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440000",
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

**Validation Rules**:
- `bookingId` must be unique
- `eventId` must reference a valid event
- `expiredAt` must be 10 minutes from `timestamp`

---

#### BookingExpired

**Producer**: `bookingService`  
**Topic**: `booking.lifecycle`  
**When to send**: When booking hold period expires (10 minutes)

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440001",
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

---

#### BookingCancelled

**Producer**: `bookingService`  
**Topic**: `booking.lifecycle`  
**When to send**: When user cancels a pending booking

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440002",
  "eventType": "BookingCancelled",
  "aggregateType": "Booking",
  "aggregateId": "456",
  "sourceService": "bookingService",
  "timestamp": "2026-06-14T10:35:00Z",
  "correlationId": "corr-abc-123",
  "payload": {
    "bookingId": 456,
    "userId": 789,
    "eventId": 123,
    "reason": "USER_CANCELLED"
  }
}
```

---

### 3.2 For inventoryService (continued)

#### PaymentSucceeded

**Producer**: `paymentService`  
**Topic**: `payment.events`  
**When to send**: After payment gateway confirms success

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440003",
  "eventType": "PaymentSucceeded",
  "aggregateType": "Payment",
  "aggregateId": "789",
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

---

#### PaymentFailed

**Producer**: `paymentService`  
**Topic**: `payment.events`  
**When to send**: After payment fails (card declined, expired, etc.)

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440004",
  "eventType": "PaymentFailed",
  "aggregateType": "Payment",
  "aggregateId": "789",
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

---

### 3.3 For notificationService

#### TicketIssued

**Producer**: `inventoryService` (our service)  
**Topic**: `ticket.lifecycle`  
**When to send**: After tickets are created

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440005",
  "eventType": "TicketIssued",
  "aggregateType": "Ticket",
  "aggregateId": "1001",
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

---

### 3.4 For inventoryService (Catalog Sync)

#### TicketTypeUpdated

**Producer**: `eventService`  
**Topic**: `event.catalog`  
**When to send**: When ticket type is created, updated, or status changes

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440006",
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

---

## 4. Events We Produce

### 4.1 inventoryService Events

| Event | Topic | When | Consumer |
|-------|-------|------|----------|
| `SeatsHeld` | `inventory.updates` | After Redis seat hold | `bookingService`, `analytics` |
| `SeatsConfirmed` | `inventory.updates` | After DB seat confirm | `bookingService`, `analytics` |
| `SeatsReleased` | `inventory.updates` | After seat release | `bookingService`, `analytics` |
| `InventoryLow` | `inventory.updates` | When available < 10 | `eventService`, `analytics` |
| `TicketIssued` | `ticket.lifecycle` | After ticket creation | `notificationService` |
| `TicketCheckedIn` | `ticket.lifecycle` | After check-in | `analytics` |

### 4.2 notificationService Events

| Event | Topic | When | Consumer |
|-------|-------|------|----------|
| `EmailSent` | `notification.requests` | After email sent | `analytics` |
| `SMSSent` | `notification.requests` | After SMS sent | `analytics` |
| `PushSent` | `notification.requests` | After push sent | `analytics` |
| `NotificationFailed` | `notification.dlq` | After 3 retries fail | `monitoring`, `ops` |

---

## 5. Event Processing Order

### 5.1 Happy Path

```
T+0s  User clicks "Buy" → Gateway
T+0s  Gateway → bookingService: create booking
T+0s  bookingService produces: BookingCreated → booking.lifecycle
T+0s  inventoryService consumes: BookingCreated → holds seats
T+0s  inventoryService produces: SeatsHeld → inventory.updates
T+0s  bookingService consumes: SeatsHeld → updates booking status

T+5s  User completes payment → paymentService
T+5s  paymentService produces: PaymentSucceeded → payment.events
T+5s  inventoryService consumes: PaymentSucceeded → confirms seats
T+5s  inventoryService produces: SeatsConfirmed → inventory.updates
T+5s  inventoryService produces: TicketIssued → ticket.lifecycle
T+5s  bookingService consumes: SeatsConfirmed → marks booking PAID
T+5s  notificationService consumes: TicketIssued → sends email
T+5s  notificationService produces: EmailSent → notification.requests

Total: ~5 seconds from click to ticket email
```

### 5.2 Failure Path

```
T+0s  User clicks "Buy" → Gateway
T+0s  bookingService produces: BookingCreated
T+0s  inventoryService holds seats → produces: SeatsHeld

T+5s  User payment fails → paymentService
T+5s  paymentService produces: PaymentFailed
T+5s  inventoryService consumes: PaymentFailed → releases seats
T+5s  inventoryService produces: SeatsReleased
T+5s  bookingService consumes: PaymentFailed → marks booking FAILED
T+5s  notificationService consumes: PaymentFailed → sends failure email

T+10s  Booking hold expires (fallback)
T+10s  bookingService produces: BookingExpired
T+10s  inventoryService consumes: BookingExpired → releases seats
T+10s  inventoryService produces: SeatsReleased
```

---

## 6. Kafka Consumer Configuration

### 6.1 inventoryService Consumers

```yaml
spring:
  kafka:
    consumer:
      group-id: inventory-service-group
      auto-offset-reset: earliest
      isolation-level: read_committed
    listener:
      ack-mode: record
      concurrency: 6

# Topic subscriptions
inventory.service:
  consumed-topics:
    - booking.lifecycle
    - payment.events
    - event.catalog
```

### 6.2 notificationService Consumers

```yaml
spring:
  kafka:
    consumer:
      group-id: notification-service-group
      auto-offset-reset: earliest
      isolation-level: read_committed
    listener:
      ack-mode: record
      concurrency: 3

# Topic subscriptions
notification.service:
  consumed-topics:
    - booking.lifecycle
    - payment.events
    - ticket.lifecycle
```

---

## 7. Error Handling Contract

### 7.1 What Happens If...

| Scenario | Behavior | Recovery |
|----------|----------|----------|
| **Kafka is down** | Events accumulate in Outbox tables | Relay job retries every 5s |
| **Consumer crashes** | Consumer group rebalances | New consumer picks up from last offset |
| **Event processing fails** | Inbox status = `FAILED` | Retry after 5 minutes |
| **Duplicate event** | `eventId` unique constraint prevents double processing | Idempotent |
| **Event arrives out of order** | Partition key ensures sequential processing per aggregate | Kafka ordering guarantee |

### 7.2 Dead Letter Queue

```
Failed events (after 3 retries) go to:
  Topic: notification.dlq
  
DLQ consumers:
  - Monitoring dashboard
  - Operations team alerts
  - Manual reprocessing tool
```

---

## 8. Testing Integration

### 8.1 Before Production

Other teams must verify:

```bash
# 1. Produce test event
kafka-console-producer --bootstrap-server kafka:29092 \
  --topic booking.lifecycle \
  --property "parse.key=true" \
  --property "key.separator=:"

# Send:
456:{"eventId":"test-001","eventType":"BookingCreated",...}

# 2. Verify inventoryService consumed
# Check inventory_inbox_event table

# 3. Verify inventoryService produced
kafka-console-consumer --bootstrap-server kafka:29092 \
  --topic inventory.updates \
  --from-beginning

# 4. Verify notificationService consumed
# Check notification_inbox_event table
```

### 8.2 Integration Test Contract

```java
// Shared test class (optional)
public class KafkaEventContractTest {
    
    @Test
    void testBookingCreatedEventSchema() {
        // Given: BookingCreated event
        BookingCreatedEvent event = BookingCreatedEvent.builder()
            .bookingId(1L)
            .userId(2L)
            .eventId(3L)
            .build();
        
        // When: Serialize to JSON
        String json = objectMapper.writeValueAsString(event);
        
        // Then: Must match expected schema
        assertThat(json).contains("\"bookingId\":1");
        assertThat(json).contains("\"eventType\":\"BookingCreated\"");
    }
}
```

---

## 9. API Endpoints for Other Teams

### 9.1 inventoryService (REST)

```
GET /api/seats/availability/{eventId}/{ticketTypeId}
  Response: { available: 50, reserved: 10, sold: 40, total: 100 }
  
  Used by: bookingService (to check availability before creating booking)

GET /api/tickets/booking/{bookingId}
  Response: List<TicketDTO>
  
  Used by: bookingService (to show tickets after payment)

POST /api/checkin-logs
  Body: { ticketCode, staffId, eventId }
  Response: { result: "VALID", ticket: { ... } }
  
  Used by: Staff mobile app (via gateway)
```

### 9.2 notificationService (REST)

```
GET /api/notification-requests/{bookingId}
  Response: NotificationRequestDTO
  
  Used by: bookingService (to check notification status)

POST /api/notification-requests/{id}/retry
  Body: { channel }
  Response: { status }
  
  Used by: Admin dashboard (manual retry)
```

---

## 10. Versioning & Evolution

### 10.1 Event Schema Versioning

```
Strategy: Additive only (backward compatible)

Version 1.0 (Current):
  BookingCreated { bookingId, userId, eventId, ... }

Version 1.1 (Future):
  BookingCreated { bookingId, userId, eventId, ..., promoCode }
  
  - New field is optional
  - Old consumers ignore it
  - New consumers use it

Version 2.0 (Breaking):
  BookingCreatedV2 { bookingId, userId, eventId, ..., newField }
  
  - New event type name
  - Old and new run in parallel
  - Gradual migration
```

### 10.2 Deprecation Timeline

```
Month 1: Add new event type (e.g., BookingCreatedV2)
Month 2-3: Both types published in parallel
Month 4: Notify all consumers to migrate
Month 5: Stop publishing old type
Month 6: Remove old type from documentation
```

---

## 11. Support & Contact

### 11.1 Slack Channels

| Channel | Purpose |
|---------|---------|
| `#team-inventory` | Our team channel |
| `#team-notification` | Our team channel |
| `#kafka-ops` | Kafka infrastructure issues |
| `#booking-integration` | Cross-team integration |

### 11.2 On-Call

| Service | On-Call Rotation |
|---------|-----------------|
| inventoryService | Our team (weekly rotation) |
| notificationService | Our team (weekly rotation) |
| Kafka infrastructure | Our team (shared with DevOps) |

### 11.3 Escalation

```
Level 1: Slack #team-inventory
Level 2: Page on-call engineer
Level 3: Team lead
Level 4: Engineering manager
```

---

## 12. FAQ

### Q1: What if Kafka is down?
**A**: Events are stored in Outbox tables. When Kafka recovers, the relay job catches up. No events are lost.

### Q2: Can we consume events from the beginning?
**A**: Yes, set `auto-offset-reset: earliest` and reset consumer group offsets.

### Q3: How do we handle duplicate events?
**A**: We use `eventId` unique constraints in Inbox tables. Duplicate events are ignored.

### Q4: What if inventoryService is down?
**A**: Events accumulate in Kafka (7-day retention). When inventoryService recovers, it catches up from last offset.

### Q5: Can we query inventory directly via REST?
**A**: Yes, `GET /api/seats/availability/{eventId}/{ticketTypeId}` is available. But prefer Kafka for real-time updates.

### Q6: How do we add a new event type?
**A**: 
1. Propose event in `#team-inventory`
2. Add schema to this document
3. Update topic in `02-kafka-infrastructure.md`
4. Implement in code
5. Notify consumers

---

## 13. Change Log

| Date | Version | Change | Author |
|------|---------|--------|--------|
| 2026-06-14 | 1.0 | Initial release | Inventory & Notification Team |

---

## 14. Next Steps

1. Share this document with all team leads
2. Schedule integration kickoff meeting
3. Set up shared Kafka dev environment
4. Implement event schemas in code
5. Write integration tests
6. Deploy to staging
7. Run end-to-end booking flow test
8. Go to production
