package com.ticketbooking.payment.service.dto;

import com.ticketbooking.payment.domain.enumeration.PaymentProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ticketbooking.payment.domain.PaymentWebhookLog} entity.
 */
@Schema(description = "Payment Webhook Log — lưu raw webhook từ gateway\npayment_id, payment_attempt_id có thể nullable")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentWebhookLogDTO implements Serializable {

    private Long id;

    @NotNull
    private PaymentProvider provider;

    @Size(max = 512)
    private String providerEventId;

    @Size(max = 255)
    private String eventType;

    @Lob
    private String rawPayload;

    @Size(max = 1024)
    private String signature;

    @NotNull
    private Boolean processed;

    @Size(max = 1000)
    private String processingError;

    @NotNull
    private Instant receivedAt;

    private Instant processedAt;

    private PaymentDTO payment;

    private PaymentAttemptDTO attempt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentProvider getProvider() {
        return provider;
    }

    public void setProvider(PaymentProvider provider) {
        this.provider = provider;
    }

    public String getProviderEventId() {
        return providerEventId;
    }

    public void setProviderEventId(String providerEventId) {
        this.providerEventId = providerEventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getRawPayload() {
        return rawPayload;
    }

    public void setRawPayload(String rawPayload) {
        this.rawPayload = rawPayload;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public String getProcessingError() {
        return processingError;
    }

    public void setProcessingError(String processingError) {
        this.processingError = processingError;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    public PaymentAttemptDTO getAttempt() {
        return attempt;
    }

    public void setAttempt(PaymentAttemptDTO attempt) {
        this.attempt = attempt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentWebhookLogDTO)) {
            return false;
        }

        PaymentWebhookLogDTO paymentWebhookLogDTO = (PaymentWebhookLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentWebhookLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentWebhookLogDTO{" +
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
            ", payment=" + getPayment() +
            ", attempt=" + getAttempt() +
            "}";
    }
}
