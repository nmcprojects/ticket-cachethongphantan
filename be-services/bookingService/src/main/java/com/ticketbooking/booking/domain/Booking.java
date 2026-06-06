package com.ticketbooking.booking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ticketbooking.booking.domain.enumeration.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Booking (đơn đặt vé)
 * user_id    -> auth_db.users.id (logical)
 * event_id   -> event_db.events.id (logical)
 * payment_id -> payment_db.payments.id (logical)
 */
@Entity
@Table(name = "booking")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Size(max = 255)
    @Column(name = "customer_email", length = 255, nullable = false)
    private String customerEmail;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @NotNull
    @Size(max = 500)
    @Column(name = "event_title", length = 500, nullable = false)
    private String eventTitle;

    @NotNull
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @NotNull
    @Size(max = 10)
    @Column(name = "currency", length = 10, nullable = false)
    private String currency;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @Column(name = "payment_id")
    private Long paymentId;

    @Size(max = 2048)
    @Column(name = "payment_url", length = 2048)
    private String paymentUrl;

    @Column(name = "expired_at")
    private Instant expiredAt;

    @Column(name = "paid_at")
    private Instant paidAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking")
    @JsonIgnoreProperties(value = { "booking" }, allowSetters = true)
    private Set<BookingItem> items = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking")
    @JsonIgnoreProperties(value = { "booking" }, allowSetters = true)
    private Set<BookingStatusHistory> statusHistories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Booking id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Booking userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCustomerEmail() {
        return this.customerEmail;
    }

    public Booking customerEmail(String customerEmail) {
        this.setCustomerEmail(customerEmail);
        return this;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public Booking eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return this.eventTitle;
    }

    public Booking eventTitle(String eventTitle) {
        this.setEventTitle(eventTitle);
        return this;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Booking totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Booking currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BookingStatus getStatus() {
        return this.status;
    }

    public Booking status(BookingStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Long getPaymentId() {
        return this.paymentId;
    }

    public Booking paymentId(Long paymentId) {
        this.setPaymentId(paymentId);
        return this;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentUrl() {
        return this.paymentUrl;
    }

    public Booking paymentUrl(String paymentUrl) {
        this.setPaymentUrl(paymentUrl);
        return this;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public Instant getExpiredAt() {
        return this.expiredAt;
    }

    public Booking expiredAt(Instant expiredAt) {
        this.setExpiredAt(expiredAt);
        return this;
    }

    public void setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Instant getPaidAt() {
        return this.paidAt;
    }

    public Booking paidAt(Instant paidAt) {
        this.setPaidAt(paidAt);
        return this;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Booking createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Booking updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<BookingItem> getItems() {
        return this.items;
    }

    public void setItems(Set<BookingItem> bookingItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setBooking(null));
        }
        if (bookingItems != null) {
            bookingItems.forEach(i -> i.setBooking(this));
        }
        this.items = bookingItems;
    }

    public Booking items(Set<BookingItem> bookingItems) {
        this.setItems(bookingItems);
        return this;
    }

    public Booking addItems(BookingItem bookingItem) {
        this.items.add(bookingItem);
        bookingItem.setBooking(this);
        return this;
    }

    public Booking removeItems(BookingItem bookingItem) {
        this.items.remove(bookingItem);
        bookingItem.setBooking(null);
        return this;
    }

    public Set<BookingStatusHistory> getStatusHistories() {
        return this.statusHistories;
    }

    public void setStatusHistories(Set<BookingStatusHistory> bookingStatusHistories) {
        if (this.statusHistories != null) {
            this.statusHistories.forEach(i -> i.setBooking(null));
        }
        if (bookingStatusHistories != null) {
            bookingStatusHistories.forEach(i -> i.setBooking(this));
        }
        this.statusHistories = bookingStatusHistories;
    }

    public Booking statusHistories(Set<BookingStatusHistory> bookingStatusHistories) {
        this.setStatusHistories(bookingStatusHistories);
        return this;
    }

    public Booking addStatusHistories(BookingStatusHistory bookingStatusHistory) {
        this.statusHistories.add(bookingStatusHistory);
        bookingStatusHistory.setBooking(this);
        return this;
    }

    public Booking removeStatusHistories(BookingStatusHistory bookingStatusHistory) {
        this.statusHistories.remove(bookingStatusHistory);
        bookingStatusHistory.setBooking(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Booking)) {
            return false;
        }
        return getId() != null && getId().equals(((Booking) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Booking{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", customerEmail='" + getCustomerEmail() + "'" +
            ", eventId=" + getEventId() +
            ", eventTitle='" + getEventTitle() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", currency='" + getCurrency() + "'" +
            ", status='" + getStatus() + "'" +
            ", paymentId=" + getPaymentId() +
            ", paymentUrl='" + getPaymentUrl() + "'" +
            ", expiredAt='" + getExpiredAt() + "'" +
            ", paidAt='" + getPaidAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
