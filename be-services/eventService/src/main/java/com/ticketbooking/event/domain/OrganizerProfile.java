package com.ticketbooking.event.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Organizer profile — auth_user_id is logical ref to auth_db.users.id
 */
@Entity
@Table(name = "organizer_profile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrganizerProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "auth_user_id", nullable = false)
    private Long authUserId;

    @NotNull
    @Size(max = 255)
    @Column(name = "organization_name", length = 255, nullable = false)
    private String organizationName;

    @NotNull
    @Size(max = 255)
    @Column(name = "contact_email", length = 255, nullable = false)
    private String contactEmail;

    @Size(max = 20)
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organizer")
    @JsonIgnoreProperties(value = { "ticketTypes", "organizer" }, allowSetters = true)
    private Set<Event> events = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrganizerProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthUserId() {
        return this.authUserId;
    }

    public OrganizerProfile authUserId(Long authUserId) {
        this.setAuthUserId(authUserId);
        return this;
    }

    public void setAuthUserId(Long authUserId) {
        this.authUserId = authUserId;
    }

    public String getOrganizationName() {
        return this.organizationName;
    }

    public OrganizerProfile organizationName(String organizationName) {
        this.setOrganizationName(organizationName);
        return this;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public OrganizerProfile contactEmail(String contactEmail) {
        this.setContactEmail(contactEmail);
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public OrganizerProfile contactPhone(String contactPhone) {
        this.setContactPhone(contactPhone);
        return this;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public OrganizerProfile createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public OrganizerProfile updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Event> getEvents() {
        return this.events;
    }

    public void setEvents(Set<Event> events) {
        if (this.events != null) {
            this.events.forEach(i -> i.setOrganizer(null));
        }
        if (events != null) {
            events.forEach(i -> i.setOrganizer(this));
        }
        this.events = events;
    }

    public OrganizerProfile events(Set<Event> events) {
        this.setEvents(events);
        return this;
    }

    public OrganizerProfile addEvents(Event event) {
        this.events.add(event);
        event.setOrganizer(this);
        return this;
    }

    public OrganizerProfile removeEvents(Event event) {
        this.events.remove(event);
        event.setOrganizer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizerProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((OrganizerProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizerProfile{" +
            "id=" + getId() +
            ", authUserId=" + getAuthUserId() +
            ", organizationName='" + getOrganizationName() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", contactPhone='" + getContactPhone() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
