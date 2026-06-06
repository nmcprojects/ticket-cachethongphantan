package com.ticketbooking.notification.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ticketbooking.notification.domain.enumeration.NotificationProvider;
import com.ticketbooking.notification.domain.enumeration.NotificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Email Notification — một lần gửi email cho user
 * user_id    -> auth_db.users.id (logical)
 * booking_id -> booking_db.bookings.id (logical)
 */
@Entity
@Table(name = "email_notification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @NotNull
    @Size(max = 255)
    @Column(name = "recipient_email", length = 255, nullable = false)
    private String recipientEmail;

    @NotNull
    @Size(max = 500)
    @Column(name = "subject", length = 500, nullable = false)
    private String subject;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private NotificationProvider provider;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    @Size(max = 512)
    @Column(name = "provider_message_id", length = 512)
    private String providerMessageId;

    @Size(max = 1000)
    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @Column(name = "sent_at")
    private Instant sentAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "notification")
    @JsonIgnoreProperties(value = { "notification" }, allowSetters = true)
    private Set<EmailNotificationItem> items = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmailNotification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public EmailNotification userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookingId() {
        return this.bookingId;
    }

    public EmailNotification bookingId(Long bookingId) {
        this.setBookingId(bookingId);
        return this;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getRecipientEmail() {
        return this.recipientEmail;
    }

    public EmailNotification recipientEmail(String recipientEmail) {
        this.setRecipientEmail(recipientEmail);
        return this;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSubject() {
        return this.subject;
    }

    public EmailNotification subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return this.content;
    }

    public EmailNotification content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NotificationProvider getProvider() {
        return this.provider;
    }

    public EmailNotification provider(NotificationProvider provider) {
        this.setProvider(provider);
        return this;
    }

    public void setProvider(NotificationProvider provider) {
        this.provider = provider;
    }

    public NotificationStatus getStatus() {
        return this.status;
    }

    public EmailNotification status(NotificationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public String getProviderMessageId() {
        return this.providerMessageId;
    }

    public EmailNotification providerMessageId(String providerMessageId) {
        this.setProviderMessageId(providerMessageId);
        return this;
    }

    public void setProviderMessageId(String providerMessageId) {
        this.providerMessageId = providerMessageId;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public EmailNotification errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getSentAt() {
        return this.sentAt;
    }

    public EmailNotification sentAt(Instant sentAt) {
        this.setSentAt(sentAt);
        return this;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public EmailNotification createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public EmailNotification updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<EmailNotificationItem> getItems() {
        return this.items;
    }

    public void setItems(Set<EmailNotificationItem> emailNotificationItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setNotification(null));
        }
        if (emailNotificationItems != null) {
            emailNotificationItems.forEach(i -> i.setNotification(this));
        }
        this.items = emailNotificationItems;
    }

    public EmailNotification items(Set<EmailNotificationItem> emailNotificationItems) {
        this.setItems(emailNotificationItems);
        return this;
    }

    public EmailNotification addItems(EmailNotificationItem emailNotificationItem) {
        this.items.add(emailNotificationItem);
        emailNotificationItem.setNotification(this);
        return this;
    }

    public EmailNotification removeItems(EmailNotificationItem emailNotificationItem) {
        this.items.remove(emailNotificationItem);
        emailNotificationItem.setNotification(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailNotification)) {
            return false;
        }
        return getId() != null && getId().equals(((EmailNotification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailNotification{" +
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
