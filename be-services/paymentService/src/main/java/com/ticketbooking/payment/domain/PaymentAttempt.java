package com.ticketbooking.payment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ticketbooking.payment.domain.enumeration.PaymentAttemptStatus;
import com.ticketbooking.payment.domain.enumeration.PaymentProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Payment Attempt — mỗi lần thử thanh toán qua gateway
 */
@Entity
@Table(name = "payment_attempt")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentAttempt implements Serializable {

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
    @Column(name = "provider_checkout_session_id", length = 512)
    private String providerCheckoutSessionId;

    @Size(max = 512)
    @Column(name = "provider_payment_id", length = 512)
    private String providerPaymentId;

    @Size(max = 2048)
    @Column(name = "checkout_url", length = 2048)
    private String checkoutUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentAttemptStatus status;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "attempt")
    @JsonIgnoreProperties(value = { "payment", "attempt" }, allowSetters = true)
    private Set<PaymentWebhookLog> webhookLogs = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "attempts", "webhookLogs" }, allowSetters = true)
    private Payment payment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentAttempt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentProvider getProvider() {
        return this.provider;
    }

    public PaymentAttempt provider(PaymentProvider provider) {
        this.setProvider(provider);
        return this;
    }

    public void setProvider(PaymentProvider provider) {
        this.provider = provider;
    }

    public String getProviderCheckoutSessionId() {
        return this.providerCheckoutSessionId;
    }

    public PaymentAttempt providerCheckoutSessionId(String providerCheckoutSessionId) {
        this.setProviderCheckoutSessionId(providerCheckoutSessionId);
        return this;
    }

    public void setProviderCheckoutSessionId(String providerCheckoutSessionId) {
        this.providerCheckoutSessionId = providerCheckoutSessionId;
    }

    public String getProviderPaymentId() {
        return this.providerPaymentId;
    }

    public PaymentAttempt providerPaymentId(String providerPaymentId) {
        this.setProviderPaymentId(providerPaymentId);
        return this;
    }

    public void setProviderPaymentId(String providerPaymentId) {
        this.providerPaymentId = providerPaymentId;
    }

    public String getCheckoutUrl() {
        return this.checkoutUrl;
    }

    public PaymentAttempt checkoutUrl(String checkoutUrl) {
        this.setCheckoutUrl(checkoutUrl);
        return this;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }

    public PaymentAttemptStatus getStatus() {
        return this.status;
    }

    public PaymentAttempt status(PaymentAttemptStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PaymentAttemptStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public PaymentAttempt createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public PaymentAttempt updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<PaymentWebhookLog> getWebhookLogs() {
        return this.webhookLogs;
    }

    public void setWebhookLogs(Set<PaymentWebhookLog> paymentWebhookLogs) {
        if (this.webhookLogs != null) {
            this.webhookLogs.forEach(i -> i.setAttempt(null));
        }
        if (paymentWebhookLogs != null) {
            paymentWebhookLogs.forEach(i -> i.setAttempt(this));
        }
        this.webhookLogs = paymentWebhookLogs;
    }

    public PaymentAttempt webhookLogs(Set<PaymentWebhookLog> paymentWebhookLogs) {
        this.setWebhookLogs(paymentWebhookLogs);
        return this;
    }

    public PaymentAttempt addWebhookLogs(PaymentWebhookLog paymentWebhookLog) {
        this.webhookLogs.add(paymentWebhookLog);
        paymentWebhookLog.setAttempt(this);
        return this;
    }

    public PaymentAttempt removeWebhookLogs(PaymentWebhookLog paymentWebhookLog) {
        this.webhookLogs.remove(paymentWebhookLog);
        paymentWebhookLog.setAttempt(null);
        return this;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public PaymentAttempt payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentAttempt)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentAttempt) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentAttempt{" +
            "id=" + getId() +
            ", provider='" + getProvider() + "'" +
            ", providerCheckoutSessionId='" + getProviderCheckoutSessionId() + "'" +
            ", providerPaymentId='" + getProviderPaymentId() + "'" +
            ", checkoutUrl='" + getCheckoutUrl() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
