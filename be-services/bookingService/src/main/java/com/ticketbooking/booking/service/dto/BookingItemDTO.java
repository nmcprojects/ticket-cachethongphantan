package com.ticketbooking.booking.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ticketbooking.booking.domain.BookingItem} entity.
 */
@Schema(
    description = "Booking Item — chi tiết vé trong đơn\nticket_type_id -> event_db.ticket_types.id (logical)\nsnapshot: ticketTypeName, unitPrice tại thời điểm mua"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookingItemDTO implements Serializable {

    private Long id;

    @NotNull
    private Long ticketTypeId;

    @NotNull
    @Size(max = 255)
    private String ticketTypeName;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal unitPrice;

    @NotNull
    private BigDecimal totalPrice;

    @NotNull
    private Instant createdAt;

    @NotNull
    private BookingDTO booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(Long ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public String getTicketTypeName() {
        return ticketTypeName;
    }

    public void setTicketTypeName(String ticketTypeName) {
        this.ticketTypeName = ticketTypeName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingItemDTO)) {
            return false;
        }

        BookingItemDTO bookingItemDTO = (BookingItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookingItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingItemDTO{" +
            "id=" + getId() +
            ", ticketTypeId=" + getTicketTypeId() +
            ", ticketTypeName='" + getTicketTypeName() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", totalPrice=" + getTotalPrice() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", booking=" + getBooking() +
            "}";
    }
}
