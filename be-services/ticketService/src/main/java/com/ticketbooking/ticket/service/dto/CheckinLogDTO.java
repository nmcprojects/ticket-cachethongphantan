package com.ticketbooking.ticket.service.dto;

import com.ticketbooking.ticket.domain.enumeration.CheckinResult;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ticketbooking.ticket.domain.CheckinLog} entity.
 */
@Schema(
    description = "Checkin Log — lịch sử check-in\nticket_id nullable (log cả lần thất bại với mã không tồn tại)\nstaff_id  -> auth_db.users.id (logical)\nevent_id  -> event_db.events.id (logical)"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CheckinLogDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String ticketCode;

    @NotNull
    private Long staffId;

    @NotNull
    private Long eventId;

    @NotNull
    private CheckinResult result;

    @Size(max = 500)
    private String message;

    @NotNull
    private Instant checkedInAt;

    private TicketDTO ticket;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public CheckinResult getResult() {
        return result;
    }

    public void setResult(CheckinResult result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(Instant checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    public TicketDTO getTicket() {
        return ticket;
    }

    public void setTicket(TicketDTO ticket) {
        this.ticket = ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CheckinLogDTO)) {
            return false;
        }

        CheckinLogDTO checkinLogDTO = (CheckinLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, checkinLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CheckinLogDTO{" +
            "id=" + getId() +
            ", ticketCode='" + getTicketCode() + "'" +
            ", staffId=" + getStaffId() +
            ", eventId=" + getEventId() +
            ", result='" + getResult() + "'" +
            ", message='" + getMessage() + "'" +
            ", checkedInAt='" + getCheckedInAt() + "'" +
            ", ticket=" + getTicket() +
            "}";
    }
}
