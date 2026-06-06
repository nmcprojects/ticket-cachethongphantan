package com.ticketbooking.ticket.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ticketbooking.ticket.domain.enumeration.CheckinResult;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Checkin Log — lịch sử check-in
 * ticket_id nullable (log cả lần thất bại với mã không tồn tại)
 * staff_id  -> auth_db.users.id (logical)
 * event_id  -> event_db.events.id (logical)
 */
@Entity
@Table(name = "checkin_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CheckinLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "ticket_code", length = 100, nullable = false)
    private String ticketCode;

    @NotNull
    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private CheckinResult result;

    @Size(max = 500)
    @Column(name = "message", length = 500)
    private String message;

    @NotNull
    @Column(name = "checked_in_at", nullable = false)
    private Instant checkedInAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "checkinLogs" }, allowSetters = true)
    private Ticket ticket;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CheckinLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketCode() {
        return this.ticketCode;
    }

    public CheckinLog ticketCode(String ticketCode) {
        this.setTicketCode(ticketCode);
        return this;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public Long getStaffId() {
        return this.staffId;
    }

    public CheckinLog staffId(Long staffId) {
        this.setStaffId(staffId);
        return this;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public CheckinLog eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public CheckinResult getResult() {
        return this.result;
    }

    public CheckinLog result(CheckinResult result) {
        this.setResult(result);
        return this;
    }

    public void setResult(CheckinResult result) {
        this.result = result;
    }

    public String getMessage() {
        return this.message;
    }

    public CheckinLog message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getCheckedInAt() {
        return this.checkedInAt;
    }

    public CheckinLog checkedInAt(Instant checkedInAt) {
        this.setCheckedInAt(checkedInAt);
        return this;
    }

    public void setCheckedInAt(Instant checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public CheckinLog ticket(Ticket ticket) {
        this.setTicket(ticket);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CheckinLog)) {
            return false;
        }
        return getId() != null && getId().equals(((CheckinLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CheckinLog{" +
            "id=" + getId() +
            ", ticketCode='" + getTicketCode() + "'" +
            ", staffId=" + getStaffId() +
            ", eventId=" + getEventId() +
            ", result='" + getResult() + "'" +
            ", message='" + getMessage() + "'" +
            ", checkedInAt='" + getCheckedInAt() + "'" +
            "}";
    }
}
