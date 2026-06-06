package com.ticketbooking.ticket.service.dto;

import com.ticketbooking.ticket.domain.enumeration.OutboxStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ticketbooking.ticket.domain.TicketOutboxEvent} entity.
 */
@Schema(description = "Outbox Events for Ticket Service\nPublish: TicketIssued, TicketCheckedIn")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketOutboxEventDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String aggregateType;

    @NotNull
    @Size(max = 255)
    private String aggregateId;

    @NotNull
    @Size(max = 100)
    private String eventType;

    @Lob
    private String payload;

    @NotNull
    private OutboxStatus status;

    @NotNull
    private Instant createdAt;

    private Instant publishedAt;

    @Size(max = 1000)
    private String errorMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
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

    public OutboxStatus getStatus() {
        return status;
    }

    public void setStatus(OutboxStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
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
        if (!(o instanceof TicketOutboxEventDTO)) {
            return false;
        }

        TicketOutboxEventDTO ticketOutboxEventDTO = (TicketOutboxEventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ticketOutboxEventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketOutboxEventDTO{" +
            "id=" + getId() +
            ", aggregateType='" + getAggregateType() + "'" +
            ", aggregateId='" + getAggregateId() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", payload='" + getPayload() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", publishedAt='" + getPublishedAt() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            "}";
    }
}
