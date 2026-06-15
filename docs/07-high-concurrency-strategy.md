# 07 — High Concurrency Strategy

> **Scope**: Handling 10,000+ concurrent users for hot ticket sales  
> **Owner**: Inventory & Notification Service Team  
> **Date**: 2026-06-14  
> **Status**: Design Document

---

## 1. Overview

This document describes how the system handles **high concurrency** scenarios, such as 10,000+ users simultaneously attempting to buy tickets for a popular event. The focus is on:

- **inventoryService**: Preventing overselling and handling seat contention
- **notificationService**: Processing high volumes of notification events without backlog
- **Kafka**: Partitioning and throughput optimization

---

## 2. Concurrency Scenarios

### 2.1 The "Hot Event" Problem

```
Event: Summer Music Festival
Total Seats: 10,000
On Sale: Friday 10:00 AM

10:00:00 AM — 0 users
10:00:01 AM — 1,000 users hit "Buy"
10:00:02 AM — 5,000 users hit "Buy"
10:00:05 AM — 10,000+ users hit "Buy"

All trying to book the same 10,000 seats simultaneously.
```

### 2.2 Risk Scenarios

| Risk | Impact | Mitigation |
|------|--------|------------|
| **Overselling** | Sell 12,000 tickets for 10,000 seats | Redis + Optimistic locking |
| **Race Conditions** | Two users get the same seat | Redis SET NX |
| **DB Overload** | 10K concurrent DB writes | Redis for holds, batch DB updates |
| **Kafka Lag** | Events pile up in queue | Partitioning, consumer concurrency |
| **Notification Storm** | 10K emails sent simultaneously | Rate limiting, throttling |

---

## 3. Inventory Service Concurrency Strategy

### 3.1 Multi-Level Defense

```
Level 1: Gateway Rate Limiting (not our scope, but documented)
  → Limit requests per user to 10/min

Level 2: Redis Atomic Seat Holds
  → SET NX EX: atomic, 10ms latency
  → If conflict: immediate retry with different seat

Level 3: Database Optimistic Locking
  → @Version on TicketTypeRead
  → If conflict: transaction rollback, retry

Level 4: Outbox + Kafka
  → Asynchronous event publishing
  → No blocking on Kafka during booking
```

### 3.2 Redis Seat Hold Algorithm

```java
@Service
public class HighConcurrencySeatService {
    
    private final StringRedisTemplate redisTemplate;
    
    private static final String SEAT_HOLD_KEY = "seat:hold:{eventId}:{seatId}";
    private static final Duration HOLD_TTL = Duration.ofMinutes(10);
    
    /**
     * Attempt to hold a seat atomically.
     * Returns true if hold succeeded, false if seat is already held.
     */
    public boolean tryHoldSeat(Long eventId, String seatId, Long bookingId) {
        String key = SEAT_HOLD_KEY.replace("{eventId}", eventId.toString())
                                    .replace("{seatId}", seatId);
        
        Boolean success = redisTemplate.opsForValue()
            .setIfAbsent(key, bookingId.toString(), HOLD_TTL);
        
        return Boolean.TRUE.equals(success);
    }
    
    /**
     * Hold multiple seats with conflict resolution.
     */
    public List<String> holdSeats(Long eventId, Long ticketTypeId, 
                                   int quantity, Long bookingId) {
        List<String> heldSeats = new ArrayList<>();
        
        for (int i = 0; i < quantity; i++) {
            String seatId = findNextAvailableSeat(eventId, ticketTypeId);
            
            if (seatId == null) {
                // No seats available — release partial holds
                releaseSeats(eventId, heldSeats);
                throw new NoSeatsAvailableException();
            }
            
            if (tryHoldSeat(eventId, seatId, bookingId)) {
                heldSeats.add(seatId);
            } else {
                // Seat taken by another user — try next
                i--; // Retry this iteration
                
                // Prevent infinite loop if all seats are gone
                if (heldSeats.size() >= getAvailableSeatCount(eventId, ticketTypeId)) {
                    releaseSeats(eventId, heldSeats);
                    throw new NoSeatsAvailableException();
                }
            }
        }
        
        return heldSeats;
    }
    
    private String findNextAvailableSeat(Long eventId, Long ticketTypeId) {
        // Query Redis for available seats
        // Or use a pre-allocated seat pool
        Set<String> allSeats = getAllSeats(eventId, ticketTypeId);
        Set<String> heldSeats = getHeldSeats(eventId, ticketTypeId);
        
        allSeats.removeAll(heldSeats);
        return allSeats.isEmpty() ? null : allSeats.iterator().next();
    }
}
```

### 3.3 Seat Pool Pre-allocation

For high-demand events, pre-allocate seat IDs in Redis:

```
# Pre-populate seat pool (run once when event goes on sale)
SADD event:123:seats:pool A1 A2 A3 ... A100 B1 B2 ... B100

# When user holds a seat:
SPOP event:123:seats:pool 1  →  Returns A1 (atomic, removes from pool)

# If user releases:
SADD event:123:seats:pool A1  →  Returns seat to pool
```

**Advantage**: `SPOP` is atomic — no race conditions, no retries.

```java
public String atomicHoldSeat(Long eventId) {
    String poolKey = "event:" + eventId + ":seats:pool";
    
    // SPOP removes and returns a random member
    String seatId = redisTemplate.opsForSet().pop(poolKey);
    
    if (seatId == null) {
        throw new NoSeatsAvailableException();
    }
    
    // Store hold with TTL
    String holdKey = "seat:hold:" + eventId + ":" + seatId;
    redisTemplate.opsForValue().set(holdKey, bookingId.toString(), HOLD_TTL);
    
    return seatId;
}
```

### 3.4 Optimistic Locking in DB

```java
@Entity
public class TicketTypeRead {
    
    @Version
    private Long version;
    
    // ... other fields
}

@Repository
public interface TicketTypeReadRepository extends JpaRepository<TicketTypeRead, Long> {
    
    @Modifying
    @Query("UPDATE TicketTypeRead t SET t.reservedQuantity = t.reservedQuantity + :qty, " +
           "t.availableQuantity = t.availableQuantity - :qty, " +
           "t.version = t.version + 1 " +
           "WHERE t.id = :id AND t.availableQuantity >= :qty")
    int incrementReserved(@Param("id") Long id, @Param("qty") int quantity);
    
    // Returns 1 if successful, 0 if version conflict or insufficient quantity
}
```

**Usage**:

```java
@Transactional
public void holdSeats(Long ticketTypeId, int quantity) {
    int updated = ticketTypeReadRepository.incrementReserved(ticketTypeId, quantity);
    
    if (updated == 0) {
        // Either:
        // 1. Version conflict (another transaction updated first)
        // 2. Insufficient available quantity
        throw new SeatUnavailableException("Seats no longer available");
    }
}
```

### 3.5 Retry Strategy

```java
@Service
public class RetryableSeatService {
    
    @Retryable(
        value = { SeatUnavailableException.class, OptimisticLockingFailureException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 100, multiplier = 2) // 100ms, 200ms, 400ms
    )
    public void holdSeatsWithRetry(Long ticketTypeId, int quantity) {
        holdSeats(ticketTypeId, quantity);
    }
}
```

---

## 4. Kafka Partitioning for Concurrency

### 4.1 Partition Strategy

| Topic | Partitions | Partition Key | Max Parallel Consumers |
|-------|-----------|---------------|------------------------|
| `inventory.updates` | 12 | `eventId` | 12 |
| `booking.lifecycle` | 12 | `bookingId` | 12 |
| `payment.events` | 6 | `paymentId` | 6 |
| `notification.requests` | 6 | `userId` | 6 |

### 4.2 Why 12 Partitions for Inventory?

```
12 partitions = 12 concurrent consumers

If 10,000 users book simultaneously:
  → Events distributed across 12 partitions
  → Each consumer processes ~833 events
  → With 6 consumers per partition: ~139 events per consumer

Total throughput: ~2,000 events/sec (with batch processing)
```

### 4.3 Consumer Concurrency

```java
@Configuration
public class KafkaConsumerConfig {
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> 
            inventoryKafkaListenerContainerFactory() {
        
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(6); // 6 threads per consumer instance
        factory.setBatchListener(true); // Batch processing
        factory.getContainerProperties().setPollTimeout(3000);
        
        return factory;
    }
}
```

### 4.4 Batch Processing

```java
@KafkaListener(
    topics = "inventory.updates",
    groupId = "inventory-service-group",
    containerFactory = "inventoryKafkaListenerContainerFactory"
)
public void onInventoryUpdates(List<ConsumerRecord<String, String>> records) {
    
    // Process batch of records
    List<InventoryEvent> events = records.stream()
        .map(record -> parseEvent(record.value()))
        .collect(Collectors.toList());
    
    // Batch update in DB
    batchUpdateInventory(events);
    
    // Acknowledge all at once
    ack.acknowledge();
}
```

---

## 5. Notification Service Concurrency

### 5.1 Notification Rate Limiting

```java
@Component
public class NotificationRateLimiter {
    
    private final StringRedisTemplate redisTemplate;
    
    // Rate limit: 100 emails/minute per user
    public boolean canSendEmail(Long userId) {
        String key = "notification:rate:email:" + userId;
        
        Long current = redisTemplate.opsForValue().increment(key);
        
        if (current == 1) {
            // First request, set TTL
            redisTemplate.expire(key, Duration.ofMinutes(1));
        }
        
        return current <= 100;
    }
}
```

### 5.2 Throttled Email Sending

```java
@Service
public class ThrottledEmailService {
    
    private final RateLimiter rateLimiter = RateLimiter.create(100.0); // 100/sec
    
    public void sendEmail(EmailMessage message) {
        rateLimiter.acquire(); // Block if rate exceeded
        emailGateway.send(message);
    }
}
```

### 5.3 Priority Queue

For high-priority notifications (e.g., payment failure), use a priority topic:

```
notification.requests.high    ← Payment failures, booking expiry
notification.requests.normal  ← Booking confirmations, ticket issuance
notification.requests.low     ← Marketing, newsletters
```

---

## 6. Caching Strategy

### 6.1 Redis Cache Layers

```
Layer 1: Seat Availability (Hot Path)
  Key: seat:available:{eventId}:{ticketTypeId}
  Value: {count}
  TTL: 1 minute (refreshed from DB)

Layer 2: Event Catalog (Read-Heavy)
  Key: event:{eventId}:tickettypes
  Value: List<TicketTypeDTO>
  TTL: 5 minutes

Layer 3: User Notifications (Rate Limiting)
  Key: notification:rate:{channel}:{userId}
  Value: {count}
  TTL: 1 minute
```

### 6.2 Cache Invalidation

```java
@CacheEvict(value = "seatAvailability", key = "#eventId + '-' + #ticketTypeId")
public void confirmSeats(Long eventId, Long ticketTypeId, int quantity) {
    // ... business logic
}

@CachePut(value = "seatAvailability", key = "#eventId + '-' + #ticketTypeId")
public Integer getAvailableSeats(Long eventId, Long ticketTypeId) {
    return ticketTypeReadRepository.getAvailableQuantity(ticketTypeId);
}
```

---

## 7. Load Testing Plan

### 7.1 Test Scenarios

| Scenario | Users | Duration | Expected Result |
|----------|-------|----------|----------------|
| **Normal Load** | 100 concurrent | 5 min | All successful, no errors |
| **High Load** | 1,000 concurrent | 10 min | < 1% error rate, no overselling |
| **Peak Load** | 10,000 concurrent | 15 min | < 5% error rate, no overselling |
| **Stress Test** | 20,000 concurrent | 5 min | System degrades gracefully |

### 7.2 Gatling Test Script

```scala
// InventoryServiceLoadTest.scala
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class InventoryServiceLoadTest extends Simulation {
  
  val httpProtocol = http
    .baseUrl("http://localhost:9005")
    .acceptHeader("application/json")
  
  val scn = scenario("Book Seats")
    .exec(http("Hold Seats")
      .post("/api/seats/hold")
      .body(StringBody("""{"eventId": 123, "ticketTypeId": 101, "quantity": 2}"""))
      .check(status.is(200))
    )
  
  setUp(
    scn.inject(
      rampUsers(10000).during(60) // 10K users over 60 seconds
    )
  ).protocols(httpProtocol)
}
```

### 7.3 Success Criteria

- **Seat Inventory Accuracy**: 100% — no overselling
- **Response Time**: P95 < 500ms for seat hold
- **Error Rate**: < 1% for 1K users, < 5% for 10K users
- **Kafka Lag**: < 1000 messages behind

---

## 8. Monitoring & Alerts

### 8.1 Key Metrics Dashboard

```yaml
# Grafana dashboard
panels:
  - title: "Seat Hold Latency"
    query: histogram_quantile(0.95, rate(seat_hold_latency_bucket[5m]))
    
  - title: "Kafka Consumer Lag"
    query: kafka_consumer_group_lag{group="inventory-service-group"}
    
  - title: "Redis Hit Rate"
    query: redis_keyspace_hits / (redis_keyspace_hits + redis_keyspace_misses)
    
  - title: "Outbox Pending Events"
    query: outbox_pending_count{service="inventoryService"}
    
  - title: "Notification Queue Depth"
    query: kafka_consumer_group_lag{group="notification-service-group"}
```

### 8.2 Alert Rules

```yaml
# Prometheus alerts
groups:
  - name: inventory-alerts
    rules:
      - alert: HighSeatHoldLatency
        expr: histogram_quantile(0.95, seat_hold_latency_bucket) > 1
        for: 5m
        annotations:
          summary: "Seat hold latency is high"
          
      - alert: KafkaConsumerLag
        expr: kafka_consumer_group_lag > 1000
        for: 5m
        annotations:
          summary: "Kafka consumer lag is high"
          
      - alert: RedisConnectionFailure
        expr: redis_connected_clients == 0
        for: 1m
        annotations:
          summary: "Redis connection lost"
          
      - alert: NotificationQueueBacklog
        expr: kafka_consumer_group_lag{group="notification-service-group"} > 5000
        for: 10m
        annotations:
          summary: "Notification queue backlog"
```

---

## 9. Scaling Strategy

### 9.1 Horizontal Scaling

```
Phase 1: Single Instance
  inventoryService: 1 instance
  notificationService: 1 instance
  Kafka: 1 broker

Phase 2: High Load
  inventoryService: 3 instances (load balanced)
  notificationService: 3 instances
  Kafka: 3 brokers (replication factor = 3)
  
Phase 3: Kubernetes
  inventoryService: 5-10 pods (HPA based on CPU/memory)
  notificationService: 5-10 pods
  Kafka: 5 brokers, 3 Zookeepers
  Redis: Cluster mode (3 master + 3 replica)
```

### 9.2 Auto-Scaling Rules

```yaml
# Kubernetes HPA
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: inventory-service
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: inventory-service
  minReplicas: 3
  maxReplicas: 10
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
    - type: Pods
      pods:
        metric:
          name: kafka_consumer_lag
        target:
          type: AverageValue
          averageValue: "1000"
```

---

## 10. Failure Recovery

### 10.1 Redis Failure

```
Scenario: Redis cluster goes down

Impact:
  - Seat holds cannot be made
  - Availability cache is stale

Fallback:
  1. Check DB directly (slower, but accurate)
  2. Use DB pessimistic locking (SELECT FOR UPDATE)
  3. Queue holds for retry when Redis recovers

Circuit Breaker:
  - If Redis down > 30s, switch to DB-only mode
  - If Redis down > 5min, reject new bookings
```

### 10.2 Kafka Failure

```
Scenario: Kafka cluster goes down

Impact:
  - Outbox events accumulate
  - Inbox events cannot be consumed

Fallback:
  1. Outbox events stay in DB (no data loss)
  2. When Kafka recovers, relay catches up
  3. If Kafka down > 1 hour, use REST fallback
     (only for critical operations)
```

### 10.3 DB Failure

```
Scenario: PostgreSQL goes down

Impact:
  - Cannot persist seat holds
  - Cannot create tickets

Fallback:
  1. Redis holds are still valid (10-min TTL)
  2. When DB recovers, reconcile Redis holds with DB
  3. If DB down > 10 min, release all holds
```

---

## 11. Implementation Checklist

- [ ] Implement Redis `SPOP` seat pool
- [ ] Implement `@Version` optimistic locking
- [ ] Configure `@Retryable` for seat operations
- [ ] Set up Kafka consumer concurrency (6 threads)
- [ ] Implement batch Kafka listener
- [ ] Add Redis rate limiting for notifications
- [ ] Configure notification priority queues
- [ ] Add Redis caching for seat availability
- [ ] Write Gatling load tests
- [ ] Set up Grafana dashboards
- [ ] Configure Prometheus alerts
- [ ] Test failure scenarios (Redis/Kafka/DB down)
- [ ] Document scaling procedures

---

## 12. Next Steps

1. Implement Redis seat pool pre-allocation
2. Add `@Version` to `TicketTypeRead` and `SeatHold`
3. Configure Kafka consumer concurrency
4. Implement batch processing
5. Set up Redis rate limiting
6. Write and run load tests
7. Configure monitoring and alerts
8. Test failure recovery scenarios
