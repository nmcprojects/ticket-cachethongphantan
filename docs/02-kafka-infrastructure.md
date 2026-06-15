# 02 — Kafka Infrastructure Design

> **Scope**: Apache Kafka setup, topics, partitions, and Spring Kafka configuration  
> **Owner**: Inventory & Notification Service Team  
> **Date**: 2026-06-14  
> **Status**: Design Document

---

## 1. Overview

This document describes the Kafka infrastructure for the Ticket Booking System. We are adding **Kafka + Zookeeper** to the existing Docker Compose setup. Other teams' services remain untouched — they will consume/produce events via Kafka as documented in `08-team-integration-guide.md`.

---

## 2. Docker Compose Configuration

### 2.1 Additions to `docker-compose.yml`

```yaml
version: '3.8'

services:
  # ... existing services unchanged ...

  # ============================================================
  # ZOOKEEPER (Kafka coordination)
  # ============================================================
  zookeeper:
    image: confluentinc/cp-zookeeper:7.6.0
    container_name: zookeeper
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - '2181:2181'
    networks:
      - jhipster-network

  # ============================================================
  # KAFKA (message bus)
  # ============================================================
  kafka:
    image: confluentinc/cp-kafka:7.6.0
    container_name: kafka
    depends_on:
      - zookeeper
    ports:
      - '9092:9092'
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: 'false'
    volumes:
      - kafka_data:/var/lib/kafka/data
    networks:
      - jhipster-network

  # ============================================================
  # POSTGRESQL (for our services only)
  # ============================================================
  postgres:
    image: postgres:16-alpine
    container_name: postgres
    environment:
      POSTGRES_USER: ticketbooking
      POSTGRES_PASSWORD: ticketbooking
      POSTGRES_DB: ticketbooking
    ports:
      - '5432:5432'
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - jhipster-network

  # ... rest of existing services ...

volumes:
  mysql_data:
    driver: local
  kafka_data:
    driver: local
  postgres_data:
    driver: local

networks:
  jhipster-network:
    driver: bridge
```

### 2.2 Why Confluent Platform?

| Reason | Explanation |
|--------|-------------|
| **Community Edition** | Free, production-ready Kafka distribution |
| **Version 7.6.0** | Based on Kafka 3.7.x, latest stable |
| **Pre-built image** | No need to compile or configure Kafka manually |
| **KRaft ready** | Future migration path (when Zookeeper is deprecated) |

---

## 3. Topic Definitions

### 3.1 Core Topics

```yaml
# Booking Service produces
booking.lifecycle:
  partitions: 12
  replication.factor: 1  # (single broker for dev, increase for prod)
  retention.ms: 604800000  # 7 days
  cleanup.policy: delete
  events:
    - BookingCreated
    - BookingPaymentFailed
    - BookingCancelled
    - BookingExpired

# Payment Service produces
payment.events:
  partitions: 6
  replication.factor: 1
  retention.ms: 604800000
  cleanup.policy: delete
  events:
    - PaymentSucceeded
    - PaymentFailed
    - PaymentExpired

# Inventory Service produces (OUR SERVICE)
inventory.updates:
  partitions: 12
  replication.factor: 1
  retention.ms: 604800000
  cleanup.policy: delete
  events:
    - SeatsHeld
    - SeatsConfirmed
    - SeatsReleased
    - InventoryLow

# Ticket/Issuance Service produces
ticket.lifecycle:
  partitions: 6
  replication.factor: 1
  retention.ms: 604800000
  cleanup.policy: delete
  events:
    - TicketIssued
    - TicketCheckedIn

# Notification Service produces (OUR SERVICE)
notification.requests:
  partitions: 6
  replication.factor: 1
  retention.ms: 259200000  # 3 days
  cleanup.policy: delete
  events:
    - EmailSent
    - SMSSent
    - PushSent
    - NotificationFailed

# Dead Letter Queue
notification.dlq:
  partitions: 3
  replication.factor: 1
  retention.ms: 1209600000  # 14 days
  cleanup.policy: delete
  events:
    - AllFailedNotifications

# Event Catalog updates (eventService produces)
event.catalog:
  partitions: 3
  replication.factor: 1
  retention.ms: 604800000
  cleanup.policy: compact  # Keep latest state for each key
  events:
    - EventPublished
    - EventCancelled
    - TicketTypeUpdated
    - TicketTypeSoldOut
```

### 3.2 Topic Creation Script

```bash
# init-topics.sh — run once after Kafka starts
#!/bin/bash

KAFKA="docker exec -it kafka kafka-topics --bootstrap-server kafka:29092"

# Core business topics
$KAFKA --create --if-not-exists --topic booking.lifecycle --partitions 12 --replication-factor 1
$KAFKA --create --if-not-exists --topic payment.events --partitions 6 --replication-factor 1
$KAFKA --create --if-not-exists --topic inventory.updates --partitions 12 --replication-factor 1
$KAFKA --create --if-not-exists --topic ticket.lifecycle --partitions 6 --replication-factor 1
$KAFKA --create --if-not-exists --topic notification.requests --partitions 6 --replication-factor 1

# Dead letter queue
$KAFKA --create --if-not-exists --topic notification.dlq --partitions 3 --replication-factor 1

# Event catalog (compact topic)
$KAFKA --create --if-not-exists --topic event.catalog --partitions 3 --replication-factor 1 \
  --config cleanup.policy=compact --config min.cleanable.dirty.ratio=0.5

# Verify
$KAFKA --list
```

---

## 4. Partitioning Strategy

### 4.1 Why Partition by `eventId`?

| Topic | Partition Key | Rationale |
|-------|---------------|-----------|
| `inventory.updates` | `eventId` | All seat operations for an event go to same partition → sequential processing |
| `booking.lifecycle` | `bookingId` | Booking events are independent |
| `payment.events` | `paymentId` | Payment events are independent |
| `notification.requests` | `userId` | All notifications for a user go to same partition |

### 4.2 Sequential Processing Guarantee

For `inventory.updates`, using `eventId` as partition key ensures:

```
Event ID = 123 → Partition 5
Event ID = 456 → Partition 2

All seat operations for Event 123 are processed sequentially:
  SeatsHeld → SeatsConfirmed → SeatsReleased

This prevents race conditions within the same event.
```

### 4.3 Partition Assignment

```java
// Producer configuration
props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "org.apache.kafka.clients.producer.internals.DefaultPartitioner");

// Or custom partitioner for inventory.updates
public class EventIdPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, 
                         Object value, byte[] valueBytes, Cluster cluster) {
        int numPartitions = cluster.partitionCountForTopic(topic);
        // key = eventId (String)
        return Math.abs(key.hashCode()) % numPartitions;
    }
}
```

---

## 5. Spring Kafka Configuration

### 5.1 inventoryService `application.yml`

```yaml
spring:
  kafka:
    bootstrap-servers: kafka:29092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      acks: all
      retries: 3
      enable-idempotence: true
      transactional-id: inventory-service-producer
    consumer:
      group-id: inventory-service-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: com.ticketbooking.inventory.event
      isolation-level: read_committed
    listener:
      ack-mode: record
      concurrency: 6  # 6 consumer threads

  # PostgreSQL datasource
  datasource:
    url: jdbc:postgresql://postgres:5432/inventoryservice
    username: ticketbooking
    password: ticketbooking
    driver-class-name: org.postgresql.Driver

  jpa:
    database: postgresql
    hibernate:
      ddl-auto: none
    properties:
      hibernate.dialect: org.hibernate.dialect.PostgreSQLDialect
```

### 5.2 notificationService `application.yml`

```yaml
spring:
  kafka:
    bootstrap-servers: kafka:29092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      acks: all
      retries: 3
      enable-idempotence: true
      transactional-id: notification-service-producer
    consumer:
      group-id: notification-service-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: com.ticketbooking.notification.event
      isolation-level: read_committed
    listener:
      ack-mode: record
      concurrency: 3

  # PostgreSQL datasource
  datasource:
    url: jdbc:postgresql://postgres:5432/notificationservice
    username: ticketbooking
    password: ticketbooking
    driver-class-name: org.postgresql.Driver

  jpa:
    database: postgresql
    hibernate:
      ddl-auto: none
    properties:
      hibernate.dialect: org.hibernate.dialect.PostgreSQLDialect
```

### 5.3 Kafka Configuration Properties Explained

| Property | Value | Rationale |
|----------|-------|-----------|
| `acks: all` | Wait for all replicas | Strong durability guarantee |
| `retries: 3` | Retry 3 times | Transient failure recovery |
| `enable-idempotence: true` | Exactly-once semantics | No duplicate messages on retry |
| `transactional-id` | Unique per service | Enables Kafka transactions |
| `isolation-level: read_committed` | Only read committed | No reading uncommitted transactions |
| `auto-offset-reset: earliest` | Start from beginning | Catch up on missed events |
| `ack-mode: record` | Ack after each record | At-least-once per message |
| `concurrency: 6` | 6 listener threads | Parallel processing of different partitions |

---

## 6. Consumer Group Design

### 6.1 inventoryService Consumer Groups

```java
// Consumer Group: inventory-service-group
// Subscribed topics:
//   - booking.lifecycle (BookingCreated, BookingExpired)
//   - payment.events (PaymentSucceeded, PaymentFailed)
//   - event.catalog (TicketTypeUpdated)

@KafkaListener(
    topics = "booking.lifecycle",
    groupId = "inventory-service-booking-group",
    containerFactory = "bookingKafkaListenerContainerFactory"
)
public void onBookingEvent(BookingEvent event) { ... }

@KafkaListener(
    topics = "payment.events",
    groupId = "inventory-service-payment-group",
    containerFactory = "paymentKafkaListenerContainerFactory"
)
public void onPaymentEvent(PaymentEvent event) { ... }

@KafkaListener(
    topics = "event.catalog",
    groupId = "inventory-service-catalog-group",
    containerFactory = "catalogKafkaListenerContainerFactory"
)
public void onCatalogEvent(CatalogEvent event) { ... }
```

### 6.2 notificationService Consumer Groups

```java
// Consumer Group: notification-service-group
// Subscribed topics:
//   - booking.lifecycle (BookingCreated)
//   - payment.events (PaymentSucceeded, PaymentFailed)
//   - ticket.lifecycle (TicketIssued)

@KafkaListener(
    topics = "booking.lifecycle",
    groupId = "notification-service-booking-group"
)
public void onBookingEvent(BookingEvent event) { ... }

@KafkaListener(
    topics = "payment.events",
    groupId = "notification-service-payment-group"
)
public void onPaymentEvent(PaymentEvent event) { ... }

@KafkaListener(
    topics = "ticket.lifecycle",
    groupId = "notification-service-ticket-group"
)
public void onTicketEvent(TicketEvent event) { ... }
```

---

## 7. Monitoring & Operations

### 7.1 Kafka Metrics (Prometheus)

```yaml
# Add to application.yml for both services
management:
  metrics:
    enable:
      kafka: true
```

### 7.2 Key Metrics to Monitor

| Metric | Alert Threshold |
|--------|----------------|
| `kafka.consumer.lag` | > 1000 messages |
| `kafka.producer.record.error.rate` | > 0.01 |
| `kafka.consumer.records.consumed.rate` | < 1 msg/sec (stuck consumer) |

### 7.3 Kafka UI (Optional)

```yaml
# Add to docker-compose.yml for dev
kafka-ui:
  image: provectuslabs/kafka-ui:latest
  container_name: kafka-ui
  ports:
    - '8080:8080'
  environment:
    KAFKA_CLUSTERS_0_NAME: local
    KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:29092
  networks:
    - jhipster-network
```

---

## 8. Security

### 8.1 Network Isolation

- Kafka is only accessible within the `jhipster-network` Docker network
- Port `9092` is exposed for local development tools only
- In production, Kafka should have SSL/TLS and SASL authentication

### 8.2 Transactional IDs

- Each service has a unique `transactional.id` prefix
- This enables exactly-once semantics for producer transactions

```
inventory-service-producer-{uuid}
notification-service-producer-{uuid}
```

---

## 9. Disaster Recovery

### 9.1 Topic Retention

| Topic | Retention | Recovery |
|-------|-----------|----------|
| `booking.lifecycle` | 7 days | Re-process from offset |
| `payment.events` | 7 days | Re-process from offset |
| `inventory.updates` | 7 days | Re-process from offset |
| `notification.requests` | 3 days | Re-process from offset |
| `notification.dlq` | 14 days | Manual re-processing |

### 9.2 Offset Management

- Consumer offsets are stored in Kafka (`__consumer_offsets` topic)
- If a consumer group is reset, use `kafka-consumer-groups` CLI:

```bash
# Reset to earliest (re-process all)
kafka-consumer-groups --bootstrap-server kafka:29092 \
  --group inventory-service-booking-group \
  --topic booking.lifecycle \
  --reset-offsets --to-earliest --execute

# Reset to latest (skip all)
kafka-consumer-groups --bootstrap-server kafka:29092 \
  --group inventory-service-booking-group \
  --topic booking.lifecycle \
  --reset-offsets --to-latest --execute
```

---

## 10. POM Dependencies

### 10.1 inventoryService `pom.xml` additions

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.3</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 10.2 notificationService `pom.xml` additions

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.3</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

---

## 11. Testing Strategy

### 11.1 Embedded Kafka for Unit Tests

```java
@TestConfiguration
public class KafkaTestConfig {
    
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        // Embedded Kafka producer
    }
    
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        // Embedded Kafka consumer
    }
}

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "booking.lifecycle", "inventory.updates" })
public class InventoryServiceKafkaTest {
    // Test producer/consumer integration
}
```

---

## 12. Next Steps

1. Add Kafka + Zookeeper + PostgreSQL to `docker-compose.yml`
2. Run `init-topics.sh` to create topics
3. Add `spring-kafka` + PostgreSQL dependencies to POMs
4. Configure `application.yml` for Kafka
5. Implement producers and consumers (see `06-outbox-pattern-implementation.md`)
