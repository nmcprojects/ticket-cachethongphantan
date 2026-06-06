package com.ticketbooking.notification.service.dto;

import com.ticketbooking.notification.domain.enumeration.NotificationProvider;
import com.ticketbooking.notification.domain.enumeration.NotificationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ticketbooking.notification.domain.EmailNotification} entity.
 */
@Schema(
    description = "Email Notification — một lần gửi email cho user\nuser_id    -> auth_db.users.id (logical)\nbooking_id -> booking_db.bookings.id (logical)"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailNotificationDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long bookingId;

    @NotNull
    @Size(max = 255)
    private String recipientEmail;

    @NotNull
    @Size(max = 500)
    private String subject;

    @Lob
    private String content;

    @NotNull
    private NotificationProvider provider;

    @NotNull
    private NotificationStatus status;

    @Size(max = 512)
    private String providerMessageId;

    @Size(max = 1000)
    private String errorMessage;

    private Instant sentAt;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NotificationProvider getProvider() {
        return provider;
    }

    public void setProvider(NotificationProvider provider) {
        this.provider = provider;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public String getProviderMessageId() {
        return providerMessageId;
    }

    public void setProviderMessageId(String providerMessageId) {
        this.providerMessageId = providerMessageId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
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
        if (!(o instanceof EmailNotificationDTO)) {
            return false;
        }

        EmailNotificationDTO emailNotificationDTO = (EmailNotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, emailNotificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailNotificationDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", bookingId=" + getBookingId() +
            ", recipientEmail='" + getRecipientEmail() + "'" +
            ", subject='" + getSubject() + "'" +
            ", content='" + getContent() + "'" +
            ", provider='" + getProvider() + "'" +
            ", status='" + getStatus() + "'" +
            ", providerMessageId='" + getProviderMessageId() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", sentAt='" + getSentAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
