package com.ticketbooking.event.service.dto;

import com.ticketbooking.event.domain.enumeration.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ticketbooking.event.domain.TicketReservation} entity.
 */
@Schema(
    description = "Ticket Reservation — tạm giữ vé khi customer tạo booking\nbooking_id is logical ref to booking_db.bookings.id\nuser_id    is logical ref to auth_db.users.id"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketReservationDTO implements Serializable {

    private Long id;

    @NotNull
    private Long bookingId;

    @NotNull
    private Long userId;

    @NotNull
    private Integer quantity;

    @NotNull
    private ReservationStatus status;

    @NotNull
    private Instant expiresAt;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private TicketTypeDTO ticketType;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
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

    public TicketTypeDTO getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketTypeDTO ticketType) {
        this.ticketType = ticketType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketReservationDTO)) {
            return false;
        }

        TicketReservationDTO ticketReservationDTO = (TicketReservationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ticketReservationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketReservationDTO{" +
            "id=" + getId() +
            ", bookingId=" + getBookingId() +
            ", userId=" + getUserId() +
            ", quantity=" + getQuantity() +
            ", status='" + getStatus() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", ticketType=" + getTicketType() +
            "}";
    }
}
