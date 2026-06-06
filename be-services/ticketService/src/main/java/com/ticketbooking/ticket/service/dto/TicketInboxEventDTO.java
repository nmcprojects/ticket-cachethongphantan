package com.ticketbooking.ticket.service.dto;

import com.ticketbooking.ticket.domain.enumeration.EventProcessStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ticketbooking.ticket.domain.TicketInboxEvent} entity.
 */
@Schema(description = "Inbox Events for Ticket Service\nNhận: BookingPaid")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketInboxEventDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String sourceService;

    @NotNull
    @Size(max = 255)
    private String eventId;

    @NotNull
    @Size(max = 100)
    private String eventType;

    @Lob
    private String payload;

    @NotNull
    private EventProcessStatus status;

    @NotNull
    private Instant receivedAt;

    private Instant processedAt;

    @Size(max = 1000)
    private String errorMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceService() {
        return sourceService;
    }

    public void setSourceService(String sourceService) {
        this.sourceService = sourceService;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public EventProcessStatus getStatus() {
        return status;
    }

    public void setStatus(EventProcessStatus status) {
        this.status = status;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketInboxEventDTO)) {
            return false;
        }

        TicketInboxEventDTO ticketInboxEventDTO = (TicketInboxEventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ticketInboxEventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketInboxEventDTO{" +
            "id=" + getId() +
            ", sourceService='" + getSourceService() + "'" +
            ", eventId='" + getEventId() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", payload='" + getPayload() + "'" +
            ", status='" + getStatus() + "'" +
            ", receivedAt='" + getReceivedAt() + "'" +
            ", processedAt='" + getProcessedAt() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            "}";
    }
}
