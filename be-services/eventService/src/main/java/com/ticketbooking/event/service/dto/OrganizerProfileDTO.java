package com.ticketbooking.event.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ticketbooking.event.domain.OrganizerProfile} entity.
 */
@Schema(description = "Organizer profile — auth_user_id is logical ref to auth_db.users.id")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrganizerProfileDTO implements Serializable {

    private Long id;

    @NotNull
    private Long authUserId;

    @NotNull
    @Size(max = 255)
    private String organizationName;

    @NotNull
    @Size(max = 255)
    private String contactEmail;

    @Size(max = 20)
    private String contactPhone;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(Long authUserId) {
        this.authUserId = authUserId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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
        if (!(o instanceof OrganizerProfileDTO)) {
            return false;
        }

        OrganizerProfileDTO organizerProfileDTO = (OrganizerProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, organizerProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizerProfileDTO{" +
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
