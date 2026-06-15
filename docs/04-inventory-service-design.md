# 04 — Inventory Service Design

> **Scope**: Seat hold, confirm, release logic with strong consistency  
> **Owner**: Inventory & Notification Service Team  
> **Date**: 2026-06-14  
> **Status**: Design Document

---

## 1. Overview

This document details the design of the **inventoryService** (renamed from `ticketService`, port 9005). The service manages:

- **Seat inventory** (hold, confirm, release) with strong consistency
- **Ticket issuance** (existing JHipster entity)
- **Check-in** (existing JHipster entity)
- **Read model sync** (maintains `TicketTypeRead` from `eventService` via Kafka)

---

## 2. Service Rename Impact

### 2.1 What Changes

| Area | From | To |
|------|------|-----|
| Directory | `be-services/ticketService/` | `be-services/inventoryService/` |
| POM artifactId | `ticket-service` | `inventory-service` |
| Package | `com.ticketbooking.ticket` | `com.ticketbooking.inventory` |
| App class | `TicketServiceApp` | `InventoryServiceApp` |
| DB name | `ticketservice` | `inventoryservice` |
| Eureka appname | `ticketservice` | `inventoryservice` |
| Docker container | `ticket-service` | `inventory-service` |

### 2.2 What Does NOT Change

- **Existing entities**: `Ticket`, `CheckinLog` remain in the same database schema
- **Existing REST APIs**: `/api/tickets`, `/api/checkin-logs` keep same paths
- **Existing business logic**: Ticket creation and check-in logic unchanged
- **Port**: Still `9005`

---

## 3. New Entities

### 3.1 SeatHold

Redis-backed seat hold with optional persistent audit.

```java
@Entity
@Table(name = "seat_hold")
public class SeatHold implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private Long eventId;
    
    @NotNull
    private Long ticketTypeId;
    
    @NotNull
    private Long bookingId;
    
    @NotNull
    private Long userId;
    
    @NotNull
    private String seatId;
    
    private String seatNumber;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private SeatHoldStatus status; // HELD, CONFIRMED, RELEASED, EXPIRED
    
    @NotNull
    private Instant holdExpiration;
    
    private Instant confirmedAt;
    
    private Instant releasedAt;
    
    @NotNull
    private Instant createdAt;
    
    @Version
    private Long version; // Optimistic locking
}

public enum SeatHoldStatus {
    HELD, CONFIRMED, RELEASED, EXPIRED
}
```

### 3.2 TicketTypeRead

Read-only copy of `eventService.TicketType`, synced via Kafka.

```java
@Entity
@Table(name = "ticket_type_read")
public class TicketTypeRead implements Serializable {
    
    @Id
    private Long id; // Same ID as eventService.TicketType
    
    @NotNull
    private Long eventId;
    
    @NotNull
    private String name;
    
    private String description;
    
    @NotNull
    private BigDecimal price;
    
    @NotNull
    private String currency;
    
    @NotNull
    private Integer totalQuantity;
    
    @NotNull
    private Integer availableQuantity;
    
    @NotNull
    private Integer reservedQuantity;
    
    @NotNull
    private Integer soldQuantity;
    
    @NotNull
    private Integer maxPerOrder;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private TicketTypeStatus status; // SELLING, SOLD_OUT, DISABLED
    
    @NotNull
    private Instant updatedAt;
    
    @Version
    private Long version; // For optimistic locking
}
```

### 3.3 InventoryOutboxEvent

```java
@Entity
@Table(name = "inventory_outbox_event")
public class InventoryOutboxEvent implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String aggregateType;
    
    @NotNull
    private String aggregateId;
    
    @NotNull
    private String eventType;
    
    @NotNull
    @Lob
    private String payload;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private OutboxStatus status; // PENDING, PUBLISHED, FAILED
    
    @NotNull
    private Instant createdAt;
    
    private Instant publishedAt;
    
    private String errorMessage;
}
```

### 3.4 InventoryInboxEvent

```java
@Entity
@Table(name = "inventory_inbox_event")
public class InventoryInboxEvent implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String sourceService;
    
    @NotNull
    @Column(unique = true)
    private String eventId; // UUID from Kafka
    
    @NotNull
    private String eventType;
    
    @NotNull
    @Lob
    private String payload;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private EventProcessStatus status; // RECEIVED, PROCESSED, FAILED
    
    @NotNull
    private Instant receivedAt;
    
    private Instant processedAt;
    
    private String errorMessage;
}
```

---

## 4. Seat Inventory Flow

### 4.1 State Machine

```
         ┌─────────────┐
         │   AVAILABLE  │
         └─────────────┘
                │
                │ BookingCreated event
                │ (Redis SET NX EX)
                ▼
         ┌─────────────┐
         │    HELD      │
         │   (10 min)   │
         └─────────────┘
                │
      ┌─────────┴─────────┐
      │                   │
      │ PaymentSucceeded  │ PaymentFailed / BookingExpired
      │                   │
      ▼                   ▼
┌─────────────┐    ┌─────────────┐
│  CONFIRMED   │    │  RELEASED    │
│   (DB txn)   │    │  (Redis DEL) │
└─────────────┘    └─────────────┘
```

### 4.2 Hold Seats (Redis)

```java
@Service
public class SeatInventoryService {
    
    private final StringRedisTemplate redisTemplate;
    private final SeatHoldRepository seatHoldRepository;
    private final TicketTypeReadRepository ticketTypeReadRepository;
    private final InventoryOutboxEventRepository outboxRepository;
    
    private static final String SEAT_HOLD_KEY = "seat:hold:%d:%s"; // eventId:seatId
    private static final Duration HOLD_TTL = Duration.ofMinutes(10);
    
    @Transactional
    public void holdSeats(BookingCreatedEvent event) {
        Long eventId = event.getPayload().getEventId();
        Long ticketTypeId = event.getPayload().getItems().get(0).getTicketTypeId();
        
        // 1. Check availability in read model
        TicketTypeRead ticketType = ticketTypeReadRepository.findById(ticketTypeId)
            .orElseThrow(() -> new InventoryException("Ticket type not found"));
        
        int requested = event.getPayload().getItems().get(0).getQuantity();
        if (ticketType.getAvailableQuantity() < requested) {
            throw new InventoryException("Not enough seats available");
        }
        
        // 2. Atomic Redis holds
        for (int i = 0; i < requested; i++) {
            String seatId = generateSeatId(eventId, ticketTypeId, i);
            String key = String.format(SEAT_HOLD_KEY, eventId, seatId);
            
            Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, event.getPayload().getBookingId().toString(), HOLD_TTL);
            
            if (Boolean.FALSE.equals(success)) {
                // Seat already held — rollback previous holds
                releaseHolds(eventId, event.getPayload().getBookingId());
                throw new InventoryException("Seat conflict — please retry");
            }
            
            // 3. Persist audit trail
            SeatHold hold = new SeatHold()
                .eventId(eventId)
                .ticketTypeId(ticketTypeId)
                .bookingId(event.getPayload().getBookingId())
                .userId(event.getPayload().getUserId())
                .seatId(seatId)
                .status(SeatHoldStatus.HELD)
                .holdExpiration(Instant.now().plus(HOLD_TTL))
                .createdAt(Instant.now());
            
            seatHoldRepository.save(hold);
        }
        
        // 4. Update read model (reserved count)
        ticketTypeReadRepository.incrementReserved(ticketTypeId, requested);
        
        // 5. Publish event via Outbox
        InventoryOutboxEvent outbox = new InventoryOutboxEvent()
            .aggregateType("Inventory")
            .aggregateId("event-" + eventId)
            .eventType("SeatsHeld")
            .payload(toJson(buildSeatsHeldEvent(event, seatIds)))
            .status(OutboxStatus.PENDING)
            .createdAt(Instant.now());
        
        outboxRepository.save(outbox);
    }
}
```

### 4.3 Confirm Seats (DB Transaction)

```java
@Transactional
public void confirmSeats(PaymentSucceededEvent event) {
    Long bookingId = event.getPayload().getBookingId();
    
    // 1. Find held seats
    List<SeatHold> holds = seatHoldRepository.findByBookingIdAndStatus(bookingId, SeatHoldStatus.HELD);
    
    if (holds.isEmpty()) {
        throw new InventoryException("No held seats found for booking");
    }
    
    // 2. Verify holds haven't expired
    for (SeatHold hold : holds) {
        if (hold.getHoldExpiration().isBefore(Instant.now())) {
            throw new InventoryException("Seat hold expired");
        }
    }
    
    // 3. Optimistic locking update
    Long ticketTypeId = holds.get(0).getTicketTypeId();
    TicketTypeRead ticketType = ticketTypeReadRepository.findById(ticketTypeId)
        .orElseThrow(() -> new InventoryException("Ticket type not found"));
    
    // 4. Update seat holds to CONFIRMED
    for (SeatHold hold : holds) {
        hold.setStatus(SeatHoldStatus.CONFIRMED);
        hold.setConfirmedAt(Instant.now());
        
        // 5. Remove Redis key
        String key = String.format(SEAT_HOLD_KEY, hold.getEventId(), hold.getSeatId());
        redisTemplate.delete(key);
    }
    
    // 6. Update read model: reserved--, sold++
    ticketTypeReadRepository.decrementReserved(ticketTypeId, holds.size());
    ticketTypeReadRepository.incrementSold(ticketTypeId, holds.size());
    
    // 7. Create tickets
    List<Ticket> tickets = holds.stream()
        .map(hold -> createTicket(hold, event))
        .collect(Collectors.toList());
    
    // 8. Publish events via Outbox
    InventoryOutboxEvent seatsConfirmed = new InventoryOutboxEvent()
        .eventType("SeatsConfirmed")
        .payload(toJson(buildSeatsConfirmedEvent(event, tickets)))
        .status(OutboxStatus.PENDING)
        .createdAt(Instant.now());
    outboxRepository.save(seatsConfirmed);
    
    // 9. Publish TicketIssued events
    for (Ticket ticket : tickets) {
        InventoryOutboxEvent ticketIssued = new InventoryOutboxEvent()
            .eventType("TicketIssued")
            .payload(toJson(buildTicketIssuedEvent(ticket)))
            .status(OutboxStatus.PENDING)
            .createdAt(Instant.now());
        outboxRepository.save(ticketIssued);
    }
    
    // 10. Check inventory low
    TicketTypeRead updated = ticketTypeReadRepository.findById(ticketTypeId).get();
    if (updated.getAvailableQuantity() <= 10) {
        InventoryOutboxEvent inventoryLow = new InventoryOutboxEvent()
            .eventType("InventoryLow")
            .payload(toJson(buildInventoryLowEvent(updated)))
            .status(OutboxStatus.PENDING)
            .createdAt(Instant.now());
        outboxRepository.save(inventoryLow);
    }
}
```

### 4.4 Release Seats

```java
@Transactional
public void releaseSeats(BookingEvent event, String reason) {
    Long bookingId = event.getPayload().getBookingId();
    
    // 1. Find held seats
    List<SeatHold> holds = seatHoldRepository.findByBookingIdAndStatus(bookingId, SeatHoldStatus.HELD);
    
    if (holds.isEmpty()) {
        log.warn("No held seats to release for booking {}", bookingId);
        return;
    }
    
    // 2. Update holds
    for (SeatHold hold : holds) {
        hold.setStatus(SeatHoldStatus.RELEASED);
        hold.setReleasedAt(Instant.now());
        
        // 3. Remove Redis key
        String key = String.format(SEAT_HOLD_KEY, hold.getEventId(), hold.getSeatId());
        redisTemplate.delete(key);
    }
    
    // 4. Update read model
    Long ticketTypeId = holds.get(0).getTicketTypeId();
    ticketTypeReadRepository.decrementReserved(ticketTypeId, holds.size());
    ticketTypeReadRepository.incrementAvailable(ticketTypeId, holds.size());
    
    // 5. Publish event via Outbox
    InventoryOutboxEvent outbox = new InventoryOutboxEvent()
        .eventType("SeatsReleased")
        .payload(toJson(buildSeatsReleasedEvent(event, reason)))
        .status(OutboxStatus.PENDING)
        .createdAt(Instant.now());
    
    outboxRepository.save(outbox);
}
```

---

## 5. Redis Key Design

```
Key Pattern: seat:hold:{eventId}:{seatId}
Value:      {bookingId}
TTL:        600 seconds (10 minutes)

Example:
  seat:hold:123:A-1  →  456
  seat:hold:123:A-2  →  456

Additional Keys:
  inventory:available:{eventId}:{ticketTypeId}  →  {count}
  inventory:reserved:{eventId}:{ticketTypeId}   →  {count}
  inventory:sold:{eventId}:{ticketTypeId}         →  {count}
```

---

## 6. Read Model Sync (CQRS)

### 6.1 Kafka Consumer for TicketType Updates

```java
@KafkaListener(
    topics = "event.catalog",
    groupId = "inventory-service-catalog-group"
)
public void onTicketTypeUpdated(TicketTypeUpdatedEvent event) {
    // 1. Idempotency check
    if (inventoryInboxRepository.existsByEventId(event.getEventId())) {
        return; // Already processed
    }
    
    // 2. Save inbox event
    InventoryInboxEvent inbox = new InventoryInboxEvent()
        .sourceService("eventService")
        .eventId(event.getEventId())
        .eventType("TicketTypeUpdated")
        .payload(toJson(event))
        .status(EventProcessStatus.RECEIVED)
        .receivedAt(Instant.now());
    
    inventoryInboxRepository.save(inbox);
    
    // 3. Update read model
    TicketTypeRead read = ticketTypeReadRepository.findById(event.getPayload().getTicketTypeId())
        .orElse(new TicketTypeRead());
    
    read.setId(event.getPayload().getTicketTypeId())
        .setEventId(event.getPayload().getEventId())
        .setName(event.getPayload().getName())
        .setDescription(event.getPayload().getDescription())
        .setPrice(event.getPayload().getPrice())
        .setCurrency(event.getPayload().getCurrency())
        .setTotalQuantity(event.getPayload().getTotalQuantity())
        .setAvailableQuantity(event.getPayload().getAvailableQuantity())
        .setMaxPerOrder(event.getPayload().getMaxPerOrder())
        .setStatus(event.getPayload().getStatus())
        .setUpdatedAt(event.getPayload().getUpdatedAt());
    
    ticketTypeReadRepository.save(read);
    
    // 4. Mark processed
    inbox.setStatus(EventProcessStatus.PROCESSED);
    inbox.setProcessedAt(Instant.now());
    inventoryInboxRepository.save(inbox);
}
```

---

## 7. API Endpoints

### 7.1 Seat Management (NEW)

```
POST /api/seats/hold
  Body: { bookingId, eventId, ticketTypeId, quantity }
  Response: { seatHoldIds, holdExpiration }

POST /api/seats/confirm
  Body: { bookingId, paymentId }
  Response: { ticketIds }

POST /api/seats/release
  Body: { bookingId, reason }
  Response: { releasedCount }

GET /api/seats/availability/{eventId}/{ticketTypeId}
  Response: { available, reserved, sold, total }
```

### 7.2 Existing Endpoints (Unchanged)

```
POST /api/tickets
GET /api/tickets
GET /api/tickets/{id}
PUT /api/tickets/{id}
DELETE /api/tickets/{id}

POST /api/checkin-logs
GET /api/checkin-logs
```

---

## 8. Strong Consistency Guarantees

### 8.1 What "Strong Consistency" Means

- **No overselling**: `availableQuantity` is always accurate
- **No double-booking**: Redis `SET NX` guarantees atomic seat holds
- **Transactional integrity**: Outbox pattern ensures events are not lost

### 8.2 Concurrency Control

```
Level 1: Redis (optimistic) → Fast, 10ms
  - SET NX EX for seat holds
  - If conflict, immediate retry

Level 2: Database (optimistic locking) → Reliable
  - @Version on SeatHold and TicketTypeRead
  - If conflict, transaction rolls back

Level 3: Outbox (at-least-once) → Durable
  - Events published after DB commit
  - Relay job retries failed publishes
```

---

## 9. Scheduled Jobs

### 9.1 Outbox Relay

```java
@Component
public class InventoryOutboxRelay {
    
    @Scheduled(fixedRate = 5000) // Every 5 seconds
    @Transactional
    public void relayPendingEvents() {
        List<InventoryOutboxEvent> pending = outboxRepository
            .findByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);
        
        for (InventoryOutboxEvent event : pending) {
            try {
                kafkaTemplate.send(
                    "inventory.updates",
                    event.getAggregateId(), // partition key
                    event.getPayload()
                ).get(); // Wait for confirmation
                
                event.setStatus(OutboxStatus.PUBLISHED);
                event.setPublishedAt(Instant.now());
            } catch (Exception e) {
                event.setStatus(OutboxStatus.FAILED);
                event.setErrorMessage(e.getMessage());
            }
            outboxRepository.save(event);
        }
    }
}
```

### 9.2 Expired Hold Cleanup

```java
@Component
public class ExpiredHoldCleanup {
    
    @Scheduled(fixedRate = 60000) // Every 1 minute
    @Transactional
    public void cleanupExpiredHolds() {
        List<SeatHold> expired = seatHoldRepository
            .findByStatusAndHoldExpirationBefore(SeatHoldStatus.HELD, Instant.now());
        
        for (SeatHold hold : expired) {
            // Release Redis key
            String key = String.format("seat:hold:%d:%s", hold.getEventId(), hold.getSeatId());
            redisTemplate.delete(key);
            
            // Update hold status
            hold.setStatus(SeatHoldStatus.EXPIRED);
            hold.setReleasedAt(Instant.now());
            
            // Update read model
            ticketTypeReadRepository.decrementReserved(hold.getTicketTypeId(), 1);
            ticketTypeReadRepository.incrementAvailable(hold.getTicketTypeId(), 1);
        }
    }
}
```

---

## 10. Error Handling

| Error | Cause | Action |
|-------|-------|--------|
| Seat conflict | Another user holds the same seat | Release partial holds, retry with new seats |
| Hold expired | Payment took longer than 10 min | Release seats, notify user |
| Optimistic lock fail | Concurrent DB update | Retry transaction (max 3) |
| Outbox publish fail | Kafka unavailable | Retry in next scheduled run |
| TicketType not found | Sync lag | Queue for retry, log warning |

---

## 11. Testing Strategy

### 11.1 Unit Tests

```java
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "inventory.updates", "booking.lifecycle" })
public class SeatInventoryServiceTest {
    
    @Test
    void testHoldSeats() {
        // Given: BookingCreated event
        // When: holdSeats()
        // Then: Redis keys created, SeatHold persisted, Outbox event created
    }
    
    @Test
    void testConcurrentSeatHold() {
        // Given: Two concurrent requests for same seat
        // When: Both try to hold
        // Then: Only one succeeds (Redis SET NX)
    }
    
    @Test
    void testConfirmAfterPayment() {
        // Given: Held seats
        // When: PaymentSucceeded event
        // Then: Seats confirmed, tickets created, Redis keys deleted
    }
}
```

### 11.2 Load Tests

```java
// JMeter or Gatling
// 1000 concurrent users booking same event
// Verify: no overselling, all seats accounted for
```

---

## 12. Migration Plan

### 12.1 Database Migration

```sql
-- 001_create_seat_hold.sql
CREATE TABLE seat_hold (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    ticket_type_id BIGINT NOT NULL,
    booking_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    seat_id VARCHAR(100) NOT NULL,
    seat_number VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    hold_expiration TIMESTAMP NOT NULL,
    confirmed_at TIMESTAMP,
    released_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    version BIGINT DEFAULT 0
);

CREATE INDEX idx_seat_hold_booking ON seat_hold(booking_id);
CREATE INDEX idx_seat_hold_event ON seat_hold(event_id);

-- 002_create_ticket_type_read.sql
CREATE TABLE ticket_type_read (
    id BIGINT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(21, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    total_quantity INTEGER NOT NULL,
    available_quantity INTEGER NOT NULL,
    reserved_quantity INTEGER NOT NULL,
    sold_quantity INTEGER NOT NULL,
    max_per_order INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT DEFAULT 0
);

CREATE INDEX idx_ticket_type_read_event ON ticket_type_read(event_id);

-- 003_create_inventory_outbox.sql
CREATE TABLE inventory_outbox_event (
    id BIGSERIAL PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    published_at TIMESTAMP,
    error_message VARCHAR(1000)
);

CREATE INDEX idx_outbox_status ON inventory_outbox_event(status, created_at);

-- 004_create_inventory_inbox.sql
CREATE TABLE inventory_inbox_event (
    id BIGSERIAL PRIMARY KEY,
    source_service VARCHAR(100) NOT NULL,
    event_id VARCHAR(255) NOT NULL UNIQUE,
    event_type VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    received_at TIMESTAMP NOT NULL,
    processed_at TIMESTAMP,
    error_message VARCHAR(1000)
);

CREATE INDEX idx_inbox_event_id ON inventory_inbox_event(event_id);
```

---

## 13. Next Steps

1. Create Liquibase changelogs for new tables
2. Implement `SeatInventoryService` with Redis integration
3. Implement `TicketTypeRead` sync consumer
4. Set up Outbox relay scheduled job
5. Add Redis container to Docker Compose
6. Configure PostgreSQL datasource
7. Implement API endpoints
8. Write unit and integration tests
