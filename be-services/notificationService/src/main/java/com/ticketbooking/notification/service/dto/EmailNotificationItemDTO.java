package com.ticketbooking.notification.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ticketbooking.notification.domain.EmailNotificationItem} entity.
 */
@Schema(description = "Email Notification Item — từng vé gắn trong email\nticket_id -> ticket_db.tickets.id (logical)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailNotificationItemDTO implements Serializable {

    private Long id;

    @NotNull
    private Long ticketId;

    @NotNull
    @Size(max = 100)
    private String ticketCode;

    @NotNull
    private Instant createdAt;

    @NotNull
    private EmailNotificationDTO notification;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public EmailNotificationDTO getNotification() {
        return notification;
    }

    public void setNotification(EmailNotificationDTO notification) {
        this.notification = notification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailNotificationItemDTO)) {
            return false;
        }

        EmailNotificationItemDTO emailNotificationItemDTO = (EmailNotificationItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, emailNotificationItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailNotificationItemDTO{" +
            "id=" + getId() +
            ", ticketId=" + getTicketId() +
            ", ticketCode='" + getTicketCode() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", notification=" + getNotification() +
            "}";
    }
}
