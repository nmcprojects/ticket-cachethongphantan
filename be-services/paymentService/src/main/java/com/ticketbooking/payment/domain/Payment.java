package com.ticketbooking.payment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ticketbooking.payment.domain.enumeration.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Payment — một giao dịch thanh toán
 * booking_id -> booking_db.bookings.id (logical)
 * user_id    -> auth_db.users.id (logical)
 */
@Entity
@Table(name = "payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Size(max = 10)
    @Column(name = "currency", length = 10, nullable = false)
    private String currency;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "failed_at")
    private Instant failedAt;

    @Column(name = "expired_at")
    private Instant expiredAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payment")
    @JsonIgnoreProperties(value = { "webhookLogs", "payment" }, allowSetters = true)
    private Set<PaymentAttempt> attempts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payment")
    @JsonIgnoreProperties(value = { "payment", "attempt" }, allowSetters = true)
    private Set<PaymentWebhookLog> webhookLogs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return this.bookingId;
    }

    public Payment bookingId(Long bookingId) {
        this.setBookingId(bookingId);
        return this;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Payment userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Payment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Payment currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public Payment status(PaymentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Instant getPaidAt() {
        return this.paidAt;
    }

    public Payment paidAt(Instant paidAt) {
        this.setPaidAt(paidAt);
        return this;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public Instant getFailedAt() {
        return this.failedAt;
    }

    public Payment failedAt(Instant failedAt) {
        this.setFailedAt(failedAt);
        return this;
    }

    public void setFailedAt(Instant failedAt) {
        this.failedAt = failedAt;
    }

    public Instant getExpiredAt() {
        return this.expiredAt;
    }

    public Payment expiredAt(Instant expiredAt) {
        this.setExpiredAt(expiredAt);
        return this;
    }

    public void setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Payment createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Payment updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<PaymentAttempt> getAttempts() {
        return this.attempts;
    }

    public void setAttempts(Set<PaymentAttempt> paymentAttempts) {
        if (this.attempts != null) {
            this.attempts.forEach(i -> i.setPayment(null));
        }
        if (paymentAttempts != null) {
            paymentAttempts.forEach(i -> i.setPayment(this));
        }
        this.attempts = paymentAttempts;
    }

    public Payment attempts(Set<PaymentAttempt> paymentAttempts) {
        this.setAttempts(paymentAttempts);
        return this;
    }

    public Payment addAttempts(PaymentAttempt paymentAttempt) {
        this.attempts.add(paymentAttempt);
        paymentAttempt.setPayment(this);
        return this;
    }

    public Payment removeAttempts(PaymentAttempt paymentAttempt) {
        this.attempts.remove(paymentAttempt);
        paymentAttempt.setPayment(null);
        return this;
    }

    public Set<PaymentWebhookLog> getWebhookLogs() {
        return this.webhookLogs;
    }

    public void setWebhookLogs(Set<PaymentWebhookLog> paymentWebhookLogs) {
        if (this.webhookLogs != null) {
            this.webhookLogs.forEach(i -> i.setPayment(null));
        }
        if (paymentWebhookLogs != null) {
            paymentWebhookLogs.forEach(i -> i.setPayment(this));
        }
        this.webhookLogs = paymentWebhookLogs;
    }

    public Payment webhookLogs(Set<PaymentWebhookLog> paymentWebhookLogs) {
        this.setWebhookLogs(paymentWebhookLogs);
        return this;
    }

    public Payment addWebhookLogs(PaymentWebhookLog paymentWebhookLog) {
        this.webhookLogs.add(paymentWebhookLog);
        paymentWebhookLog.setPayment(this);
        return this;
    }

    public Payment removeWebhookLogs(PaymentWebhookLog paymentWebhookLog) {
        this.webhookLogs.remove(paymentWebhookLog);
        paymentWebhookLog.setPayment(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return getId() != null && getId().equals(((Payment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", bookingId=" + getBookingId() +
            ", userId=" + getUserId() +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", status='" + getStatus() + "'" +
            ", paidAt='" + getPaidAt() + "'" +
            ", failedAt='" + getFailedAt() + "'" +
            ", expiredAt='" + getExpiredAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
