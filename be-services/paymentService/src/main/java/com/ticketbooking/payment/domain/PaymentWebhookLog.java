package com.ticketbooking.payment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ticketbooking.payment.domain.enumeration.PaymentProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Payment Webhook Log — lưu raw webhook từ gateway
 * payment_id, payment_attempt_id có thể nullable
 */
@Entity
@Table(name = "payment_webhook_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentWebhookLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private PaymentProvider provider;

    @Size(max = 512)
    @Column(name = "provider_event_id", length = 512)
    private String providerEventId;

    @Size(max = 255)
    @Column(name = "event_type", length = 255)
    private String eventType;

    @Lob
    @Column(name = "raw_payload", nullable = false)
    private String rawPayload;

    @Size(max = 1024)
    @Column(name = "signature", length = 1024)
    private String signature;

    @NotNull
    @Column(name = "processed", nullable = false)
    private Boolean processed;

    @Size(max = 1000)
    @Column(name = "processing_error", length = 1000)
    private String processingError;

    @NotNull
    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "attempts", "webhookLogs" }, allowSetters = true)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "webhookLogs", "payment" }, allowSetters = true)
    private PaymentAttempt attempt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentWebhookLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentProvider getProvider() {
        return this.provider;
    }

    public PaymentWebhookLog provider(PaymentProvider provider) {
        this.setProvider(provider);
        return this;
    }

    public void setProvider(PaymentProvider provider) {
        this.provider = provider;
    }

    public String getProviderEventId() {
        return this.providerEventId;
    }

    public PaymentWebhookLog providerEventId(String providerEventId) {
        this.setProviderEventId(providerEventId);
        return this;
    }

    public void setProviderEventId(String providerEventId) {
        this.providerEventId = providerEventId;
    }

    public String getEventType() {
        return this.eventType;
    }

    public PaymentWebhookLog eventType(String eventType) {
        this.setEventType(eventType);
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getRawPayload() {
        return this.rawPayload;
    }

    public PaymentWebhookLog rawPayload(String rawPayload) {
        this.setRawPayload(rawPayload);
        return this;
    }

    public void setRawPayload(String rawPayload) {
        this.rawPayload = rawPayload;
    }

    public String getSignature() {
        return this.signature;
    }

    public PaymentWebhookLog signature(String signature) {
        this.setSignature(signature);
        return this;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Boolean getProcessed() {
        return this.processed;
    }

    public PaymentWebhookLog processed(Boolean processed) {
        this.setProcessed(processed);
        return this;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public String getProcessingError() {
        return this.processingError;
    }

    public PaymentWebhookLog processingError(String processingError) {
        this.setProcessingError(processingError);
        return this;
    }

    public void setProcessingError(String processingError) {
        this.processingError = processingError;
    }

    public Instant getReceivedAt() {
        return this.receivedAt;
    }

    public PaymentWebhookLog receivedAt(Instant receivedAt) {
        this.setReceivedAt(receivedAt);
        return this;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

    public Instant getProcessedAt() {
        return this.processedAt;
    }

    public PaymentWebhookLog processedAt(Instant processedAt) {
        this.setProcessedAt(processedAt);
        return this;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public PaymentWebhookLog payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public PaymentAttempt getAttempt() {
        return this.attempt;
    }

    public void setAttempt(PaymentAttempt paymentAttempt) {
        this.attempt = paymentAttempt;
    }

    public PaymentWebhookLog attempt(PaymentAttempt paymentAttempt) {
        this.setAttempt(paymentAttempt);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentWebhookLog)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentWebhookLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentWebhookLog{" +
            "id=" + getId() +
            ", provider='" + getProvider() + "'" +
            ", providerEventId='" + getProviderEventId() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", rawPayload='" + getRawPayload() + "'" +
            ", signature='" + getSignature() + "'" +
            ", processed='" + getProcessed() + "'" +
            ", processingError='" + getProcessingError() + "'" +
            ", receivedAt='" + getReceivedAt() + "'" +
            ", processedAt='" + getProcessedAt() + "'" +
            "}";
    }
}
