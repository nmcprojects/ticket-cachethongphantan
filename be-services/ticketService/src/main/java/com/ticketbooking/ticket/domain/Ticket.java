package com.ticketbooking.ticket.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ticketbooking.ticket.domain.enumeration.TicketStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Ticket — vé điện tử
 * booking_id      -> booking_db.bookings.id (logical)
 * booking_item_id -> booking_db.booking_items.id (logical)
 * user_id         -> auth_db.users.id (logical)
 * event_id        -> event_db.events.id (logical)
 * ticket_type_id  -> event_db.ticket_types.id (logical)
 * snapshot: customerEmail, eventTitle, ticketTypeName
 */
@Entity
@Table(name = "ticket")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @NotNull
    @Column(name = "booking_item_id", nullable = false)
    private Long bookingItemId;

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
    @Column(name = "ticket_type_id", nullable = false)
    private Long ticketTypeId;

    @NotNull
    @Size(max = 255)
    @Column(name = "ticket_type_name", length = 255, nullable = false)
    private String ticketTypeName;

    @NotNull
    @Size(max = 100)
    @Column(name = "ticket_code", length = 100, nullable = false, unique = true)
    private String ticketCode;

    @NotNull
    @Size(max = 2048)
    @Column(name = "qr_payload", length = 2048, nullable = false)
    private String qrPayload;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status;

    @NotNull
    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    @Column(name = "checked_in_at")
    private Instant checkedInAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticket")
    @JsonIgnoreProperties(value = { "ticket" }, allowSetters = true)
    private Set<CheckinLog> checkinLogs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ticket id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return this.bookingId;
    }

    public Ticket bookingId(Long bookingId) {
        this.setBookingId(bookingId);
        return this;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getBookingItemId() {
        return this.bookingItemId;
    }

    public Ticket bookingItemId(Long bookingItemId) {
        this.setBookingItemId(bookingItemId);
        return this;
    }

    public void setBookingItemId(Long bookingItemId) {
        this.bookingItemId = bookingItemId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Ticket userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCustomerEmail() {
        return this.customerEmail;
    }

    public Ticket customerEmail(String customerEmail) {
        this.setCustomerEmail(customerEmail);
        return this;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public Ticket eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return this.eventTitle;
    }

    public Ticket eventTitle(String eventTitle) {
        this.setEventTitle(eventTitle);
        return this;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Long getTicketTypeId() {
        return this.ticketTypeId;
    }

    public Ticket ticketTypeId(Long ticketTypeId) {
        this.setTicketTypeId(ticketTypeId);
        return this;
    }

    public void setTicketTypeId(Long ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public String getTicketTypeName() {
        return this.ticketTypeName;
    }

    public Ticket ticketTypeName(String ticketTypeName) {
        this.setTicketTypeName(ticketTypeName);
        return this;
    }

    public void setTicketTypeName(String ticketTypeName) {
        this.ticketTypeName = ticketTypeName;
    }

    public String getTicketCode() {
        return this.ticketCode;
    }

    public Ticket ticketCode(String ticketCode) {
        this.setTicketCode(ticketCode);
        return this;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getQrPayload() {
        return this.qrPayload;
    }

    public Ticket qrPayload(String qrPayload) {
        this.setQrPayload(qrPayload);
        return this;
    }

    public void setQrPayload(String qrPayload) {
        this.qrPayload = qrPayload;
    }

    public TicketStatus getStatus() {
        return this.status;
    }

    public Ticket status(TicketStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Instant getIssuedAt() {
        return this.issuedAt;
    }

    public Ticket issuedAt(Instant issuedAt) {
        this.setIssuedAt(issuedAt);
        return this;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Instant getCheckedInAt() {
        return this.checkedInAt;
    }

    public Ticket checkedInAt(Instant checkedInAt) {
        this.setCheckedInAt(checkedInAt);
        return this;
    }

    public void setCheckedInAt(Instant checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Ticket createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Ticket updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<CheckinLog> getCheckinLogs() {
        return this.checkinLogs;
    }

    public void setCheckinLogs(Set<CheckinLog> checkinLogs) {
        if (this.checkinLogs != null) {
            this.checkinLogs.forEach(i -> i.setTicket(null));
        }
        if (checkinLogs != null) {
            checkinLogs.forEach(i -> i.setTicket(this));
        }
        this.checkinLogs = checkinLogs;
    }

    public Ticket checkinLogs(Set<CheckinLog> checkinLogs) {
        this.setCheckinLogs(checkinLogs);
        return this;
    }

    public Ticket addCheckinLogs(CheckinLog checkinLog) {
        this.checkinLogs.add(checkinLog);
        checkinLog.setTicket(this);
        return this;
    }

    public Ticket removeCheckinLogs(CheckinLog checkinLog) {
        this.checkinLogs.remove(checkinLog);
        checkinLog.setTicket(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket)) {
            return false;
        }
        return getId() != null && getId().equals(((Ticket) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ticket{" +
            "id=" + getId() +
            ", bookingId=" + getBookingId() +
            ", bookingItemId=" + getBookingItemId() +
            ", userId=" + getUserId() +
            ", customerEmail='" + getCustomerEmail() + "'" +
            ", eventId=" + getEventId() +
            ", eventTitle='" + getEventTitle() + "'" +
            ", ticketTypeId=" + getTicketTypeId() +
            ", ticketTypeName='" + getTicketTypeName() + "'" +
            ", ticketCode='" + getTicketCode() + "'" +
            ", qrPayload='" + getQrPayload() + "'" +
            ", status='" + getStatus() + "'" +
            ", issuedAt='" + getIssuedAt() + "'" +
            ", checkedInAt='" + getCheckedInAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
