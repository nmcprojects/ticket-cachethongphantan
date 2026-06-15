# 06 — Outbox Pattern Implementation

> **Scope**: Transactional Outbox pattern for reliable Kafka event publishing  
> **Owner**: Inventory & Notification Service Team  
> **Date**: 2026-06-14  
> **Status**: Design Document

---

## 1. Overview

The **Outbox Pattern** ensures that domain events are published to Kafka **reliably** and **atomically** with database transactions. Without this pattern, a service could:

1. Commit a database transaction
2. Crash before publishing to Kafka
3. Result: **Data inconsistency** — DB updated, but no event sent

The Outbox pattern solves this by:
1. Writing events to an **Outbox table** in the same DB transaction as the business logic
2. A separate **relay process** reads the Outbox and publishes to Kafka
3. If Kafka is down, the relay retries later

---

## 2. Why Not Use Kafka Transactions?

| Approach | Problem |
|----------|---------|
| **Kafka Transactions** | Requires 2PC (Two-Phase Commit). Complex, slow, and couples DB + Kafka. |
| **Outbox Pattern** | Decoupled. DB and Kafka are independent. Simple and reliable. |

**We use the Outbox pattern** because:
- It's simpler to implement and debug
- Works with any database (PostgreSQL, MySQL)
- No distributed transaction coordinator needed
- Battle-tested in production (e.g., Uber, Shopify, Debezium)

---

## 3. Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    HTTP Request / Kafka Event               │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  @Transactional Service Method                              │
│  ┌─────────────────────────────────────────────────────┐      │
│  │  1. Business Logic                                  │      │
│  │     • Update inventory DB                           │      │
│  │     • Create tickets                                │      │
│  │     • ...                                           │      │
│  ├─────────────────────────────────────────────────────┤      │
│  │  2. Write to Outbox Table (same transaction)         │      │
│  │     INSERT INTO inventory_outbox_event              │      │
│  │       (eventType, payload, status='PENDING')        │      │
│  ├─────────────────────────────────────────────────────┤      │
│  │  COMMIT; ← Atomic: DB + Outbox committed together  │      │
│  └─────────────────────────────────────────────────────┘      │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ (async)
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  @Scheduled Relay Process (every 5s)                        │
│  ┌─────────────────────────────────────────────────────┐      │
│  │  SELECT * FROM outbox WHERE status = 'PENDING'        │      │
│  ├─────────────────────────────────────────────────────┤      │
│  │  FOR each event:                                    │      │
│  │    • Parse payload                                  │      │
│  │    • kafkaTemplate.send(topic, key, payload)        │      │
│  │    • IF success: UPDATE status = 'PUBLISHED'        │      │
│  │    • IF fail: UPDATE status = 'FAILED', log error   │      │
│  └─────────────────────────────────────────────────────┘      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
                        ┌─────────────┐
                        │    Kafka    │
                        │   Topic     │
                        └─────────────┘
```

---

## 4. Outbox Table Schema

### 4.1 inventoryService

```sql
CREATE TABLE inventory_outbox_event (
    id BIGSERIAL PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,              -- JSON event payload
    status VARCHAR(20) NOT NULL,      -- PENDING, PUBLISHED, FAILED
    created_at TIMESTAMP NOT NULL,
    published_at TIMESTAMP,
    error_message VARCHAR(1000)
);

CREATE INDEX idx_inventory_outbox_status 
    ON inventory_outbox_event(status, created_at);
```

### 4.2 notificationService

```sql
CREATE TABLE notification_outbox_event (
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

CREATE INDEX idx_notification_outbox_status 
    ON notification_outbox_event(status, created_at);
```

---

## 5. Outbox Event Entity

### 5.1 inventoryService

```java
@Entity
@Table(name = "inventory_outbox_event")
public class InventoryOutboxEvent implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Size(max = 100)
    @Column(name = "aggregate_type", length = 100, nullable = false)
    private String aggregateType;
    
    @NotNull
    @Size(max = 255)
    @Column(name = "aggregate_id", length = 255, nullable = false)
    private String aggregateId;
    
    @NotNull
    @Size(max = 100)
    @Column(name = "event_type", length = 100, nullable = false)
    private String eventType;
    
    @NotNull
    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OutboxStatus status;
    
    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    
    @Column(name = "published_at")
    private Instant publishedAt;
    
    @Size(max = 1000)
    @Column(name = "error_message", length = 1000)
    private String errorMessage;
    
    // Getters, setters, builder methods...
}

public enum OutboxStatus {
    PENDING, PUBLISHED, FAILED
}
```

---

## 6. Business Logic + Outbox (Transactional)

### 6.1 inventoryService: Confirming Seats

```java
@Service
@Transactional
public class SeatInventoryService {
    
    private final SeatHoldRepository seatHoldRepository;
    private final TicketTypeReadRepository ticketTypeReadRepository;
    private final TicketRepository ticketRepository;
    private final InventoryOutboxEventRepository outboxRepository;
    private final StringRedisTemplate redisTemplate;
    
    public void confirmSeats(PaymentSucceededEvent paymentEvent) {
        Long bookingId = paymentEvent.getPayload().getBookingId();
        
        // 1. Business logic: find held seats
        List<SeatHold> holds = seatHoldRepository
            .findByBookingIdAndStatus(bookingId, SeatHoldStatus.HELD);
        
        if (holds.isEmpty()) {
            throw new InventoryException("No held seats found");
        }
        
        // 2. Business logic: update seat holds
        List<String> ticketIds = new ArrayList<>();
        for (SeatHold hold : holds) {
            hold.setStatus(SeatHoldStatus.CONFIRMED);
            hold.setConfirmedAt(Instant.now());
            
            // Remove Redis key
            redisTemplate.delete(
                String.format("seat:hold:%d:%s", hold.getEventId(), hold.getSeatId())
            );
            
            // Create ticket
            Ticket ticket = createTicket(hold);
            ticketRepository.save(ticket);
            ticketIds.add(ticket.getId().toString());
        }
        
        // 3. Business logic: update inventory read model
        Long ticketTypeId = holds.get(0).getTicketTypeId();
        ticketTypeReadRepository.decrementReserved(ticketTypeId, holds.size());
        ticketTypeReadRepository.incrementSold(ticketTypeId, holds.size());
        
        // 4. Write to Outbox (same transaction!)
        InventoryOutboxEvent seatsConfirmed = new InventoryOutboxEvent()
            .aggregateType("Inventory")
            .aggregateId("event-" + holds.get(0).getEventId())
            .eventType("SeatsConfirmed")
            .payload(toJson(buildSeatsConfirmedPayload(holds, ticketIds)))
            .status(OutboxStatus.PENDING)
            .createdAt(Instant.now());
        
        outboxRepository.save(seatsConfirmed);
        
        // 5. Write TicketIssued events to Outbox
        for (Ticket ticket : tickets) {
            InventoryOutboxEvent ticketIssued = new InventoryOutboxEvent()
                .aggregateType("Ticket")
                .aggregateId(ticket.getId().toString())
                .eventType("TicketIssued")
                .payload(toJson(buildTicketIssuedPayload(ticket)))
                .status(OutboxStatus.PENDING)
                .createdAt(Instant.now());
            
            outboxRepository.save(ticketIssued);
        }
        
        // 6. Transaction commits here
        // Both DB updates AND Outbox rows are committed atomically
    }
}
```

### 6.2 notificationService: Sending Notifications

```java
@Service
@Transactional
public class NotificationService {
    
    private final NotificationRequestRepository requestRepository;
    private final NotificationDeliveryRepository deliveryRepository;
    private final NotificationOutboxEventRepository outboxRepository;
    
    public void processBookingCreated(BookingCreatedEvent event) {
        // 1. Create notification request
        NotificationRequest request = new NotificationRequest()
            .eventId(event.getEventId())
            .bookingId(event.getPayload().getBookingId())
            .userId(event.getPayload().getUserId())
            .userEmail(event.getPayload().getCustomerEmail())
            .type(NotificationType.BOOKING_CONFIRMED)
            .status(NotificationStatus.PENDING)
            .createdAt(Instant.now());
        
        requestRepository.save(request);
        
        // 2. Create email delivery record
        NotificationDelivery emailDelivery = new NotificationDelivery()
            .notificationRequest(request)
            .channel(NotificationChannel.EMAIL)
            .status(DeliveryStatus.PENDING)
            .retryCount(0)
            .createdAt(Instant.now());
        
        deliveryRepository.save(emailDelivery);
        
        // 3. Write to Outbox
        NotificationOutboxEvent outbox = new NotificationOutboxEvent()
            .aggregateType("Notification")
            .aggregateId(request.getId().toString())
            .eventType("NotificationRequestCreated")
            .payload(toJson(buildNotificationRequestPayload(request)))
            .status(OutboxStatus.PENDING)
            .createdAt(Instant.now());
        
        outboxRepository.save(outbox);
    }
}
```

---

## 7. Outbox Relay (Scheduled Job)

### 7.1 inventoryService Relay

```java
@Component
@Transactional
public class InventoryOutboxRelay {
    
    private static final Logger LOG = LoggerFactory.getLogger(InventoryOutboxRelay.class);
    
    private final InventoryOutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    // Run every 5 seconds
    @Scheduled(fixedRate = 5000)
    public void relayPendingEvents() {
        List<InventoryOutboxEvent> pending = outboxRepository
            .findByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING, PageRequest.of(0, 100));
        
        for (InventoryOutboxEvent event : pending) {
            try {
                // Determine topic based on event type
                String topic = resolveTopic(event.getEventType());
                
                // Send to Kafka
                kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload())
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            // Success
                            event.setStatus(OutboxStatus.PUBLISHED);
                            event.setPublishedAt(Instant.now());
                            LOG.info("Published event {} to topic {}", 
                                event.getEventType(), topic);
                        } else {
                            // Failure
                            event.setStatus(OutboxStatus.FAILED);
                            event.setErrorMessage(ex.getMessage());
                            LOG.error("Failed to publish event {}: {}", 
                                event.getEventType(), ex.getMessage());
                        }
                        outboxRepository.save(event);
                    });
                
            } catch (Exception e) {
                event.setStatus(OutboxStatus.FAILED);
                event.setErrorMessage(e.getMessage());
                outboxRepository.save(event);
                LOG.error("Error processing outbox event {}: {}", 
                    event.getId(), e.getMessage());
            }
        }
    }
    
    private String resolveTopic(String eventType) {
        return switch (eventType) {
            case "SeatsHeld", "SeatsConfirmed", "SeatsReleased", "InventoryLow" 
                -> "inventory.updates";
            case "TicketIssued", "TicketCheckedIn" 
                -> "ticket.lifecycle";
            default -> throw new IllegalArgumentException("Unknown event type: " + eventType);
        };
    }
}
```

### 7.2 notificationService Relay

```java
@Component
@Transactional
public class NotificationOutboxRelay {
    
    private static final Logger LOG = LoggerFactory.getLogger(NotificationOutboxRelay.class);
    
    private final NotificationOutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    @Scheduled(fixedRate = 5000)
    public void relayPendingEvents() {
        List<NotificationOutboxEvent> pending = outboxRepository
            .findByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING, PageRequest.of(0, 100));
        
        for (NotificationOutboxEvent event : pending) {
            try {
                String topic = resolveTopic(event.getEventType());
                
                kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload())
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            event.setStatus(OutboxStatus.PUBLISHED);
                            event.setPublishedAt(Instant.now());
                            LOG.info("Published notification event {} to topic {}", 
                                event.getEventType(), topic);
                        } else {
                            event.setStatus(OutboxStatus.FAILED);
                            event.setErrorMessage(ex.getMessage());
                            LOG.error("Failed to publish notification event {}: {}", 
                                event.getEventType(), ex.getMessage());
                        }
                        outboxRepository.save(event);
                    });
                
            } catch (Exception e) {
                event.setStatus(OutboxStatus.FAILED);
                event.setErrorMessage(e.getMessage());
                outboxRepository.save(event);
            }
        }
    }
    
    private String resolveTopic(String eventType) {
        return switch (eventType) {
            case "EmailSent", "SMSSent", "PushSent", "NotificationRequestCreated" 
                -> "notification.requests";
            case "NotificationFailed" 
                -> "notification.dlq";
            default -> throw new IllegalArgumentException("Unknown event type: " + eventType);
        };
    }
}
```

---

## 8. Inbox Pattern (Consuming Events)

### 8.1 Idempotent Consumer

```java
@Component
public class InventoryEventConsumer {
    
    private final InventoryInboxEventRepository inboxRepository;
    private final SeatInventoryService inventoryService;
    
    @KafkaListener(topics = "booking.lifecycle", groupId = "inventory-service-booking-group")
    @Transactional
    public void onBookingCreated(BookingEvent event) {
        // 1. Idempotency check
        if (inboxRepository.existsByEventId(event.getEventId())) {
            LOG.info("Event {} already processed", event.getEventId());
            return;
        }
        
        // 2. Record in Inbox
        InventoryInboxEvent inbox = new InventoryInboxEvent()
            .sourceService(event.getSourceService())
            .eventId(event.getEventId())
            .eventType(event.getEventType())
            .payload(toJson(event))
            .status(EventProcessStatus.RECEIVED)
            .receivedAt(Instant.now());
        
        inboxRepository.save(inbox);
        
        // 3. Process business logic
        try {
            switch (event.getEventType()) {
                case "BookingCreated" -> inventoryService.holdSeats(event);
                case "BookingExpired" -> inventoryService.releaseSeats(event, "BOOKING_EXPIRED");
            }
            
            inbox.setStatus(EventProcessStatus.PROCESSED);
            inbox.setProcessedAt(Instant.now());
            
        } catch (Exception e) {
            inbox.setStatus(EventProcessStatus.FAILED);
            inbox.setErrorMessage(e.getMessage());
            LOG.error("Failed to process event {}: {}", event.getEventId(), e.getMessage());
        }
        
        inboxRepository.save(inbox);
    }
}
```

### 8.2 Inbox Table Schema

```sql
CREATE TABLE inventory_inbox_event (
    id BIGSERIAL PRIMARY KEY,
    source_service VARCHAR(100) NOT NULL,
    event_id VARCHAR(255) NOT NULL UNIQUE,  -- UUID from Kafka
    event_type VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,            -- RECEIVED, PROCESSED, FAILED
    received_at TIMESTAMP NOT NULL,
    processed_at TIMESTAMP,
    error_message VARCHAR(1000)
);

CREATE INDEX idx_inventory_inbox_event_id ON inventory_inbox_event(event_id);
```

---

## 9. Retry & Failure Handling

### 9.1 Outbox Retry

```java
@Component
public class OutboxRetryService {
    
    @Scheduled(fixedRate = 60000) // Every 1 minute
    @Transactional
    public void retryFailedEvents() {
        // Retry events that failed more than 5 minutes ago
        Instant cutoff = Instant.now().minusSeconds(300);
        
        List<InventoryOutboxEvent> failed = outboxRepository
            .findByStatusAndCreatedAtBefore(OutboxStatus.FAILED, cutoff);
        
        for (InventoryOutboxEvent event : failed) {
            event.setStatus(OutboxStatus.PENDING);
            outboxRepository.save(event);
        }
        
        LOG.info("Retried {} failed outbox events", failed.size());
    }
}
```

### 9.2 Inbox Retry

```java
@Component
public class InboxRetryService {
    
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void retryFailedInboxEvents() {
        Instant cutoff = Instant.now().minusSeconds(300);
        
        List<InventoryInboxEvent> failed = inboxRepository
            .findByStatusAndReceivedAtBefore(EventProcessStatus.FAILED, cutoff);
        
        for (InventoryInboxEvent event : failed) {
            event.setStatus(EventProcessStatus.RECEIVED);
            inboxRepository.save(event);
            // The event will be re-processed by the consumer
        }
    }
}
```

---

## 10. Outbox Cleanup

Old published events should be cleaned up periodically:

```java
@Component
public class OutboxCleanupService {
    
    @Scheduled(cron = "0 0 2 * * ?") // Every day at 2 AM
    @Transactional
    public void cleanupPublishedEvents() {
        // Delete events published more than 7 days ago
        Instant cutoff = Instant.now().minusSeconds(604800);
        
        int deleted = outboxRepository.deleteByStatusAndPublishedAtBefore(
            OutboxStatus.PUBLISHED, cutoff);
        
        LOG.info("Cleaned up {} published outbox events", deleted);
    }
}
```

---

## 11. Monitoring

### 11.1 Key Metrics

| Metric | Description | Alert Threshold |
|--------|-------------|----------------|
| `outbox.pending.count` | Number of pending events | > 1000 |
| `outbox.failed.count` | Number of failed events | > 100 |
| `outbox.relay.latency` | Time from created to published | > 30 seconds |
| `inbox.pending.count` | Number of unprocessed inbox events | > 1000 |
| `inbox.failed.count` | Number of failed inbox events | > 100 |

### 11.2 Micrometer Metrics

```java
@Component
public class OutboxMetrics {
    
    private final MeterRegistry meterRegistry;
    
    public void recordOutboxPublished(InventoryOutboxEvent event) {
        Duration latency = Duration.between(event.getCreatedAt(), Instant.now());
        
        meterRegistry.timer("outbox.relay.latency", 
                "eventType", event.getEventType())
            .record(latency);
        
        meterRegistry.counter("outbox.published", 
                "eventType", event.getEventType())
            .increment();
    }
    
    public void recordOutboxFailed(InventoryOutboxEvent event) {
        meterRegistry.counter("outbox.failed", 
                "eventType", event.getEventType(),
                "error", event.getErrorMessage())
            .increment();
    }
}
```

---

## 12. Transactional Event Listener (Alternative)

For events triggered within the same service (not cross-service), we can use Spring's `@TransactionalEventListener`:

```java
@Service
public class InventoryEventPublisher {
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSeatConfirmed(SeatConfirmedEvent event) {
        // This runs AFTER the transaction commits
        // Can be used to trigger non-DB side effects
    }
}
```

**Note**: We still use the Outbox table for Kafka publishing because:
- `@TransactionalEventListener` doesn't survive crashes
- Outbox is persistent and retryable

---

## 13. Comparison: Outbox vs Direct Kafka

| Scenario | Direct Kafka | Outbox Pattern |
|----------|-------------|----------------|
| DB commit succeeds, Kafka fails | **Inconsistent** | **Consistent** (retry later) |
| Service crashes after DB commit | **Event lost** | **Event preserved** in Outbox |
| Kafka temporarily down | **Event lost** | **Queued** in Outbox |
| Complexity | Low | Medium |
| Latency | Low | Slightly higher (5s relay) |
| **Recommended?** | No | **Yes** |

---

## 14. Implementation Checklist

- [ ] Create Outbox table Liquibase changelog
- [ ] Create Inbox table Liquibase changelog
- [ ] Implement `InventoryOutboxEvent` entity
- [ ] Implement `InventoryInboxEvent` entity
- [ ] Implement `NotificationOutboxEvent` entity
- [ ] Implement `NotificationInboxEvent` entity
- [ ] Add Outbox write to all business service methods
- [ ] Implement `InventoryOutboxRelay` scheduled job
- [ ] Implement `NotificationOutboxRelay` scheduled job
- [ ] Implement Kafka consumers with Inbox pattern
- [ ] Add retry logic for failed Outbox/Inbox events
- [ ] Add cleanup scheduled job for old published events
- [ ] Add Micrometer metrics
- [ ] Write integration tests with Embedded Kafka

---

## 15. Next Steps

1. Create Liquibase changelogs for Outbox and Inbox tables
2. Implement `InventoryOutboxEvent` entity and repository
3. Implement `InventoryOutboxRelay` with `@Scheduled`
4. Add Outbox write to `SeatInventoryService` methods
5. Implement `InventoryEventConsumer` with Inbox pattern
6. Repeat for `notificationService`
7. Test with Embedded Kafka
8. Monitor with Prometheus metrics
