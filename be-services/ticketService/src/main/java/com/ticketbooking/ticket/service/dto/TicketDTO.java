package com.ticketbooking.ticket.service.dto;

import com.ticketbooking.ticket.domain.enumeration.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ticketbooking.ticket.domain.Ticket} entity.
 */
@Schema(
    description = "Ticket — vé điện tử\nbooking_id      -> booking_db.bookings.id (logical)\nbooking_item_id -> booking_db.booking_items.id (logical)\nuser_id         -> auth_db.users.id (logical)\nevent_id        -> event_db.events.id (logical)\nticket_type_id  -> event_db.ticket_types.id (logical)\nsnapshot: customerEmail, eventTitle, ticketTypeName"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketDTO implements Serializable {

    private Long id;

    @NotNull
    private Long bookingId;

    @NotNull
    private Long bookingItemId;

    @NotNull
    private Long userId;

    @NotNull
    @Size(max = 255)
    private String customerEmail;

    @NotNull
    private Long eventId;

    @NotNull
    @Size(max = 500)
    private String eventTitle;

    @NotNull
    private Long ticketTypeId;

    @NotNull
    @Size(max = 255)
    private String ticketTypeName;

    @NotNull
    @Size(max = 100)
    private String ticketCode;

    @NotNull
    @Size(max = 2048)
    private String qrPayload;

    @NotNull
    private TicketStatus status;

    @NotNull
    private Instant issuedAt;

    private Instant checkedInAt;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getBookingItemId() {
        return bookingItemId;
    }

    public void setBookingItemId(Long bookingItemId) {
        this.bookingItemId = bookingItemId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Long getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(Long ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public String getTicketTypeName() {
        return ticketTypeName;
    }

    public void setTicketTypeName(String ticketTypeName) {
        this.ticketTypeName = ticketTypeName;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getQrPayload() {
        return qrPayload;
    }

    public void setQrPayload(String qrPayload) {
        this.qrPayload = qrPayload;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Instant getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(Instant checkedInAt) {
        this.checkedInAt = checkedInAt;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketDTO)) {
            return false;
        }

        TicketDTO ticketDTO = (TicketDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ticketDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketDTO{" +
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
