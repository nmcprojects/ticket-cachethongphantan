# 05 — Notification Service Design

> **Scope**: Multi-channel notification dispatch (Email, SMS, Push)  
> **Owner**: Inventory & Notification Service Team  
> **Date**: 2026-06-14  
> **Status**: Design Document

---

## 1. Overview

This document describes the **notificationService** (port 9006) multi-channel notification architecture. The service listens to Kafka events from other services and dispatches notifications via:

- **Email** — Thymeleaf HTML templates + plaintext fallback
- **SMS** — Abstract gateway (Twilio example)
- **Push** — Abstract gateway (Firebase FCM example)

---

## 2. Architecture Principles

| Principle | Description |
|-----------|-------------|
| **Event-Driven** | No REST API calls to trigger notifications. All triggered by Kafka events. |
| **Idempotent** | Same event processed only once. Deduplication via `eventId` unique constraint. |
| **Reliable** | At-least-once delivery. Failed notifications retry 3 times, then go to DLQ. |
| **Template-Based** | Content is rendered from Thymeleaf templates. No hardcoded messages. |
| **Multi-Channel** | Single event can trigger multiple channels (email + SMS + push). |
| **Zero Impact** | No changes to other teams' services. |

---

## 3. New Entities

### 3.1 NotificationRequest

Unified notification entity. One request per event, with multiple deliveries.

```java
@Entity
@Table(name = "notification_request")
public class NotificationRequest implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(unique = true)
    private String eventId; // UUID from Kafka event
    
    @NotNull
    private Long bookingId;
    
    @NotNull
    private Long userId;
    
    @NotNull
    private String userEmail;
    
    private String userPhone; // E.164 format: +1234567890
    
    private String userDeviceToken; // For push notifications
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationType type; // BOOKING_CONFIRMED, PAYMENT_SUCCESS, PAYMENT_FAILED, TICKET_ISSUED
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationStatus status; // PENDING, PROCESSING, COMPLETED, PARTIAL_FAILURE, FAILED
    
    @NotNull
    private Instant createdAt;
    
    private Instant processedAt;
    
    @OneToMany(mappedBy = "notificationRequest", cascade = CascadeType.ALL)
    private Set<NotificationDelivery> deliveries = new HashSet<>();
}

public enum NotificationType {
    BOOKING_CONFIRMED,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,
    TICKET_ISSUED,
    BOOKING_EXPIRED
}

public enum NotificationStatus {
    PENDING, PROCESSING, COMPLETED, PARTIAL_FAILURE, FAILED
}
```

### 3.2 NotificationDelivery

Per-channel delivery attempt.

```java
@Entity
@Table(name = "notification_delivery")
public class NotificationDelivery implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "notification_request_id")
    private NotificationRequest notificationRequest;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationChannel channel; // EMAIL, SMS, PUSH
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // PENDING, SENT, FAILED
    
    private String providerMessageId; // SendGrid message ID, Twilio SID, etc.
    
    private String errorMessage;
    
    private Integer retryCount; // 0-3
    
    private Instant sentAt;
    
    @NotNull
    private Instant createdAt;
    
    private Instant lastAttemptAt;
}

public enum NotificationChannel {
    EMAIL, SMS, PUSH
}

public enum DeliveryStatus {
    PENDING, SENT, FAILED
}
```

### 3.3 NotificationTemplate

Template registry for Thymeleaf.

```java
@Entity
@Table(name = "notification_template")
public class NotificationTemplate implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(unique = true)
    private String templateKey; // "booking-confirmation", "payment-success", etc.
    
    @NotNull
    private String subject; // For email subject line
    
    @NotNull
    @Lob
    private String htmlTemplate; // Thymeleaf HTML
    
    @Lob
    private String plainTextTemplate; // Fallback
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationChannel channel; // EMAIL, SMS, PUSH
    
    @NotNull
    private String locale; // "en", "vi", etc.
    
    @NotNull
    private Instant createdAt;
    
    private Instant updatedAt;
}
```

### 3.4 NotificationOutboxEvent

```java
@Entity
@Table(name = "notification_outbox_event")
public class NotificationOutboxEvent implements Serializable {
    
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
    private OutboxStatus status;
    
    @NotNull
    private Instant createdAt;
    
    private Instant publishedAt;
    
    private String errorMessage;
}
```

---

## 4. Event-to-Notification Mapping

### 4.1 BookingCreated → Booking Confirmation

| Field | Value |
|-------|-------|
| **Trigger** | `booking.lifecycle` / `BookingCreated` |
| **Channels** | EMAIL |
| **Template** | `booking-confirmation` |
| **Content** | "Your booking #456 for Summer Music Festival has been created. Please complete payment within 10 minutes." |

### 4.2 PaymentSucceeded → Payment Success + Ticket

| Field | Value |
|-------|-------|
| **Trigger** | `payment.events` / `PaymentSucceeded` |
| **Channels** | EMAIL, SMS |
| **Templates** | `payment-success`, `ticket-issued` |
| **Content** | Email: "Payment successful! Your tickets are attached." SMS: "Payment confirmed for booking #456." |

### 4.3 PaymentFailed → Payment Failure

| Field | Value |
|-------|-------|
| **Trigger** | `payment.events` / `PaymentFailed` |
| **Channels** | EMAIL |
| **Template** | `payment-failed` |
| **Content** | "Payment failed: {reason}. Your held seats have been released." |

### 4.4 TicketIssued → Ticket Delivery

| Field | Value |
|-------|-------|
| **Trigger** | `ticket.lifecycle` / `TicketIssued` |
| **Channels** | EMAIL |
| **Template** | `ticket-issued` |
| **Content** | "Your tickets for {eventTitle} are ready! QR codes attached." |

### 4.5 BookingExpired → Expiry Notice

| Field | Value |
|-------|-------|
| **Trigger** | `booking.lifecycle` / `BookingExpired` |
| **Channels** | EMAIL |
| **Template** | `booking-expired` |
| **Content** | "Your booking #456 has expired. Seats released." |

---

## 5. Thymeleaf Templates

### 5.1 Template Structure

```
src/main/resources/templates/notifications/
├── booking-confirmation.html
├── booking-confirmation.txt
├── payment-success.html
├── payment-success.txt
├── payment-failed.html
├── payment-failed.txt
├── ticket-issued.html
├── ticket-issued.txt
└── booking-expired.html
└── booking-expired.txt
```

### 5.2 Example: booking-confirmation.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Booking Confirmation</title>
</head>
<body>
    <h1>Booking Confirmation</h1>
    <p>Hello <span th:text="${userName}">User</span>,</p>
    <p>Your booking <strong>#<span th:text="${bookingId}">123</span></strong> 
       for <strong><span th:text="${eventTitle}">Event</span></strong> 
       has been created.</p>
    
    <h3>Booking Details</h3>
    <ul>
        <li>Event: <span th:text="${eventTitle}">Event</span></li>
        <li>Date: <span th:text="${eventDate}">Date</span></li>
        <li>Total: <span th:text="${totalAmount}">$0.00</span> <span th:text="${currency}">USD</span></li>
    </ul>
    
    <p>Please complete your payment within <strong>10 minutes</strong> to secure your seats.</p>
    
    <a th:href="${paymentUrl}" class="button">Complete Payment</a>
    
    <p>Thank you,<br>Ticket Booking Team</p>
</body>
</html>
```

### 5.3 Template Context Variables

| Template | Variables |
|----------|-----------|
| `booking-confirmation` | `userName`, `bookingId`, `eventTitle`, `eventDate`, `totalAmount`, `currency`, `paymentUrl`, `items` |
| `payment-success` | `userName`, `bookingId`, `eventTitle`, `paymentAmount`, `currency`, `paidAt`, `tickets` |
| `payment-failed` | `userName`, `bookingId`, `eventTitle`, `failureReason`, `errorMessage`, `retryUrl` |
| `ticket-issued` | `userName`, `bookingId`, `eventTitle`, `eventDate`, `eventLocation`, `tickets` (list with QR) |
| `booking-expired` | `userName`, `bookingId`, `eventTitle`, `expiredAt` |

---

## 6. Notification Processing Flow

```
Kafka Event Arrives
       │
       ▼
┌─────────────────────┐
│   1. Idempotency    │
│   Check eventId in   │
│   notification_inbox│
└─────────────────────┘
       │
       ▼ (if not exists)
┌─────────────────────┐
│   2. Parse Event    │
│   Extract booking,  │
│   user, event data  │
└─────────────────────┘
       │
       ▼
┌─────────────────────┐
│   3. Create         │
│   NotificationRequest│
│   Status: PENDING    │
└─────────────────────┘
       │
       ▼
┌─────────────────────┐
│   4. Determine      │
│   Channels &        │
│   Templates         │
└─────────────────────┘
       │
       ▼
┌─────────────────────┐
│   5. For Each       │
│   Channel:          │
│   • Render template │
│   • Send via gateway│
│   • Record delivery │
└─────────────────────┘
       │
       ▼
┌─────────────────────┐
│   6. Update Status  │
│   COMPLETED /       │
│   PARTIAL_FAILURE   │
└─────────────────────┘
       │
       ▼
┌─────────────────────┐
│   7. Publish Outbox │
│   EmailSent /       │
│   NotificationFailed │
└─────────────────────┘
```

---

## 7. Channel Gateway Design

### 7.1 Email Gateway

```java
public interface EmailGateway {
    EmailResult send(String to, String subject, String htmlBody, String plainTextBody);
}

@Component
public class SendGridEmailGateway implements EmailGateway {
    
    private final SendGrid sendGrid;
    private final String fromEmail;
    
    @Override
    public EmailResult send(String to, String subject, String htmlBody, String plainTextBody) {
        Mail mail = new Mail();
        mail.setFrom(new Email(fromEmail));
        mail.setSubject(subject);
        
        Personalization personalization = new Personalization();
        personalization.addTo(new Email(to));
        mail.addPersonalization(personalization);
        
        Content htmlContent = new Content("text/html", htmlBody);
        Content textContent = new Content("text/plain", plainTextBody);
        mail.addContent(textContent);
        mail.addContent(htmlContent);
        
        try {
            Response response = sendGrid.api(mail);
            return new EmailResult(response.getStatusCode() == 202, response.getHeaders().get("X-Message-Id"));
        } catch (IOException e) {
            return new EmailResult(false, null);
        }
    }
}
```

### 7.2 SMS Gateway

```java
public interface SmsGateway {
    SmsResult send(String toPhone, String message);
}

@Component
public class TwilioSmsGateway implements SmsGateway {
    
    private final TwilioRestClient twilio;
    private final String fromPhone;
    
    @Override
    public SmsResult send(String toPhone, String message) {
        try {
            Message msg = Message.creator(
                new PhoneNumber(toPhone),
                new PhoneNumber(fromPhone),
                message
            ).create(twilio);
            
            return new SmsResult(true, msg.getSid());
        } catch (Exception e) {
            return new SmsResult(false, null);
        }
    }
}
```

### 7.3 Push Gateway

```java
public interface PushGateway {
    PushResult send(String deviceToken, String title, String body, Map<String, String> data);
}

@Component
public class FcmPushGateway implements PushGateway {
    
    private final FirebaseMessaging firebaseMessaging;
    
    @Override
    public PushResult send(String deviceToken, String title, String body, Map<String, String> data) {
        Message message = Message.builder()
            .setToken(deviceToken)
            .setNotification(Notification.builder().setTitle(title).setBody(body).build())
            .putAllData(data)
            .build();
        
        try {
            String response = firebaseMessaging.send(message);
            return new PushResult(true, response);
        } catch (FirebaseMessagingException e) {
            return new PushResult(false, null);
        }
    }
}
```

---

## 8. Retry & Dead Letter Queue

### 8.1 Retry Strategy

```java
@Component
public class NotificationRetryService {
    
    @Scheduled(fixedRate = 30000) // Every 30 seconds
    @Transactional
    public void retryFailedDeliveries() {
        List<NotificationDelivery> failed = deliveryRepository
            .findByStatusAndRetryCountLessThan(DeliveryStatus.FAILED, 3);
        
        for (NotificationDelivery delivery : failed) {
            delivery.setRetryCount(delivery.getRetryCount() + 1);
            delivery.setLastAttemptAt(Instant.now());
            
            try {
                switch (delivery.getChannel()) {
                    case EMAIL:
                        retryEmail(delivery);
                        break;
                    case SMS:
                        retrySms(delivery);
                        break;
                    case PUSH:
                        retryPush(delivery);
                        break;
                }
            } catch (Exception e) {
                delivery.setErrorMessage(e.getMessage());
                
                if (delivery.getRetryCount() >= 3) {
                    // Move to DLQ
                    sendToDlq(delivery);
                }
            }
            
            deliveryRepository.save(delivery);
        }
    }
    
    private void sendToDlq(NotificationDelivery delivery) {
        NotificationOutboxEvent dlq = new NotificationOutboxEvent()
            .eventType("NotificationFailed")
            .payload(toJson(buildNotificationFailedEvent(delivery)))
            .status(OutboxStatus.PENDING)
            .createdAt(Instant.now());
        
        outboxRepository.save(dlq);
    }
}
```

### 8.2 Exponential Backoff

| Retry Attempt | Delay |
|---------------|-------|
| 1 | 5 seconds |
| 2 | 25 seconds |
| 3 | 125 seconds |

### 8.3 DLQ Event

```json
{
  "eventType": "NotificationFailed",
  "payload": {
    "notificationId": 456,
    "deliveryId": 789,
    "channel": "EMAIL",
    "recipient": "user@example.com",
    "template": "booking-confirmation",
    "errorMessage": "SMTP connection timeout",
    "retryCount": 3,
    "finalFailureAt": "2026-06-14T10:45:00Z"
  }
}
```

**Topic**: `notification.dlq`
**Consumers**: Operations team dashboard

---

## 9. Idempotency Implementation

```java
@KafkaListener(topics = "booking.lifecycle")
@Transactional
public void onBookingCreated(BookingEvent event) {
    // 1. Check if already processed
    Optional<NotificationInboxEvent> existing = inboxRepository.findByEventId(event.getEventId());
    if (existing.isPresent()) {
        log.info("Event {} already processed, skipping", event.getEventId());
        return;
    }
    
    // 2. Record inbox
    NotificationInboxEvent inbox = new NotificationInboxEvent()
        .sourceService("bookingService")
        .eventId(event.getEventId())
        .eventType("BookingCreated")
        .payload(toJson(event))
        .status(EventProcessStatus.RECEIVED)
        .receivedAt(Instant.now());
    inboxRepository.save(inbox);
    
    // 3. Check notification request
    Optional<NotificationRequest> request = requestRepository.findByEventId(event.getEventId());
    if (request.isPresent()) {
        log.info("Notification request already exists for {}", event.getEventId());
        inbox.setStatus(EventProcessStatus.PROCESSED);
        inbox.setProcessedAt(Instant.now());
        inboxRepository.save(inbox);
        return;
    }
    
    // 4. Process notification
    try {
        processNotification(event);
        inbox.setStatus(EventProcessStatus.PROCESSED);
    } catch (Exception e) {
        inbox.setStatus(EventProcessStatus.FAILED);
        inbox.setErrorMessage(e.getMessage());
    }
    
    inbox.setProcessedAt(Instant.now());
    inboxRepository.save(inbox);
}
```

---

## 10. API Endpoints

### 10.1 Notification Management

```
GET /api/notification-requests
  Query params: userId, bookingId, status, page, size
  Response: Page<NotificationRequestDTO>

GET /api/notification-requests/{id}
  Response: NotificationRequestDTO with deliveries

GET /api/notification-requests/{id}/deliveries
  Response: List<NotificationDeliveryDTO>

POST /api/notification-requests/{id}/retry
  Body: { channel } // optional, retry specific channel
  Response: { retryCount, status }
```

### 10.2 Template Management (Admin)

```
GET /api/notification-templates
  Response: List<NotificationTemplateDTO>

GET /api/notification-templates/{key}
  Response: NotificationTemplateDTO

PUT /api/notification-templates/{key}
  Body: { subject, htmlTemplate, plainTextTemplate }
  Response: NotificationTemplateDTO
```

---

## 11. Database Migration

```sql
-- 001_create_notification_request.sql
CREATE TABLE notification_request (
    id BIGSERIAL PRIMARY KEY,
    event_id VARCHAR(255) NOT NULL UNIQUE,
    booking_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    user_phone VARCHAR(20),
    user_device_token VARCHAR(512),
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    processed_at TIMESTAMP
);

CREATE INDEX idx_notification_request_booking ON notification_request(booking_id);
CREATE INDEX idx_notification_request_user ON notification_request(user_id);
CREATE INDEX idx_notification_request_status ON notification_request(status);

-- 002_create_notification_delivery.sql
CREATE TABLE notification_delivery (
    id BIGSERIAL PRIMARY KEY,
    notification_request_id BIGINT NOT NULL,
    channel VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    provider_message_id VARCHAR(512),
    error_message VARCHAR(1000),
    retry_count INTEGER DEFAULT 0,
    sent_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    last_attempt_at TIMESTAMP,
    FOREIGN KEY (notification_request_id) REFERENCES notification_request(id)
);

CREATE INDEX idx_delivery_request ON notification_delivery(notification_request_id);
CREATE INDEX idx_delivery_status ON notification_delivery(status, retry_count);

-- 003_create_notification_template.sql
CREATE TABLE notification_template (
    id BIGSERIAL PRIMARY KEY,
    template_key VARCHAR(100) NOT NULL UNIQUE,
    subject VARCHAR(500) NOT NULL,
    html_template TEXT NOT NULL,
    plain_text_template TEXT,
    channel VARCHAR(20) NOT NULL,
    locale VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_template_key ON notification_template(template_key);

-- 004_create_notification_outbox.sql
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

CREATE INDEX idx_notification_outbox_status ON notification_outbox_event(status, created_at);
```

---

## 12. Testing Strategy

### 12.1 Unit Tests

```java
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "booking.lifecycle", "notification.requests", "notification.dlq" })
public class NotificationServiceTest {
    
    @Test
    void testBookingCreatedNotification() {
        // Given: BookingCreated event
        // When: Kafka listener processes
        // Then: Email sent, NotificationRequest persisted, Outbox event created
    }
    
    @Test
    void testIdempotentProcessing() {
        // Given: Same eventId processed twice
        // When: Second processing attempt
        // Then: NotificationRequest created only once
    }
    
    @Test
    void testFailedNotificationRetry() {
        // Given: Email gateway fails
        // When: Retry scheduled job runs
        // Then: Retries up to 3 times, then sends to DLQ
    }
}
```

### 12.2 Template Tests

```java
@Test
void testBookingConfirmationTemplate() {
    Context context = new Context();
    context.setVariable("userName", "John");
    context.setVariable("bookingId", "456");
    context.setVariable("eventTitle", "Summer Music Festival");
    
    String html = templateEngine.process("booking-confirmation", context);
    assertThat(html).contains("Summer Music Festival");
    assertThat(html).contains("Booking Confirmation");
}
```

---

## 13. Configuration

```yaml
# application.yml
notification:
  email:
    enabled: true
    from: "noreply@ticketbooking.com"
    gateway: sendgrid # or gmail_smtp
  sms:
    enabled: true
    gateway: twilio
  push:
    enabled: true
    gateway: fcm
  retry:
    max-attempts: 3
    backoff-multiplier: 5
  templates:
    default-locale: en
```

---

## 14. Next Steps

1. Create Liquibase changelogs for new tables
2. Implement Thymeleaf templates
3. Implement `NotificationService` with Kafka listeners
4. Implement `EmailGateway` (SendGrid mock for dev)
5. Implement `SmsGateway` (Twilio mock for dev)
6. Implement `PushGateway` (FCM mock for dev)
7. Set up retry scheduled job
8. Implement Outbox relay for DLQ events
9. Write unit and integration tests
10. Add template management admin UI
