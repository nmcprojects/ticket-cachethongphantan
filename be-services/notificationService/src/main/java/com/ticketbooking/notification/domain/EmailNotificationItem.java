package com.ticketbooking.notification.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Email Notification Item — từng vé gắn trong email
 * ticket_id -> ticket_db.tickets.id (logical)
 */
@Entity
@Table(name = "email_notification_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailNotificationItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @NotNull
    @Size(max = 100)
    @Column(name = "ticket_code", length = 100, nullable = false)
    private String ticketCode;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "items" }, allowSetters = true)
    private EmailNotification notification;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmailNotificationItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketId() {
        return this.ticketId;
    }

    public EmailNotificationItem ticketId(Long ticketId) {
        this.setTicketId(ticketId);
        return this;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketCode() {
        return this.ticketCode;
    }

    public EmailNotificationItem ticketCode(String ticketCode) {
        this.setTicketCode(ticketCode);
        return this;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public EmailNotificationItem createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public EmailNotification getNotification() {
        return this.notification;
    }

    public void setNotification(EmailNotification emailNotification) {
        this.notification = emailNotification;
    }

    public EmailNotificationItem notification(EmailNotification emailNotification) {
        this.setNotification(emailNotification);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailNotificationItem)) {
            return false;
        }
        return getId() != null && getId().equals(((EmailNotificationItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailNotificationItem{" +
            "id=" + getId() +
            ", ticketId=" + getTicketId() +
            ", ticketCode='" + getTicketCode() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
