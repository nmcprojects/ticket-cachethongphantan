package com.ticketbooking.payment.service.dto;

import com.ticketbooking.payment.domain.enumeration.PaymentAttemptStatus;
import com.ticketbooking.payment.domain.enumeration.PaymentProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ticketbooking.payment.domain.PaymentAttempt} entity.
 */
@Schema(description = "Payment Attempt — mỗi lần thử thanh toán qua gateway")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentAttemptDTO implements Serializable {

    private Long id;

    @NotNull
    private PaymentProvider provider;

    @Size(max = 512)
    private String providerCheckoutSessionId;

    @Size(max = 512)
    private String providerPaymentId;

    @Size(max = 2048)
    private String checkoutUrl;

    @NotNull
    private PaymentAttemptStatus status;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private PaymentDTO payment;

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

    public String getProviderCheckoutSessionId() {
        return providerCheckoutSessionId;
    }

    public void setProviderCheckoutSessionId(String providerCheckoutSessionId) {
        this.providerCheckoutSessionId = providerCheckoutSessionId;
    }

    public String getProviderPaymentId() {
        return providerPaymentId;
    }

    public void setProviderPaymentId(String providerPaymentId) {
        this.providerPaymentId = providerPaymentId;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }

    public PaymentAttemptStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentAttemptStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentAttemptDTO)) {
            return false;
        }

        PaymentAttemptDTO paymentAttemptDTO = (PaymentAttemptDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentAttemptDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentAttemptDTO{" +
            "id=" + getId() +
            ", provider='" + getProvider() + "'" +
            ", providerCheckoutSessionId='" + getProviderCheckoutSessionId() + "'" +
            ", providerPaymentId='" + getProviderPaymentId() + "'" +
            ", checkoutUrl='" + getCheckoutUrl() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", payment=" + getPayment() +
            "}";
    }
}
