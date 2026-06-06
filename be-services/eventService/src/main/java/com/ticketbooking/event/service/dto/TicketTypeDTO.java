package com.ticketbooking.event.service.dto;

import com.ticketbooking.event.domain.enumeration.TicketTypeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ticketbooking.event.domain.TicketType} entity.
 */
@Schema(
    description = "Ticket Type (loại vé: Standard, VIP, VVIP…)\nInventory rule: totalQuantity = availableQuantity + reservedQuantity + soldQuantity"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketTypeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    @Size(max = 10)
    private String currency;

    @NotNull
    private Integer totalQuantity;

    @NotNull
    private Integer availableQuantity;

    @NotNull
    private Integer reservedQuantity;

    @NotNull
    private Integer soldQuantity;

    @NotNull
    private Integer maxPerOrder;

    @NotNull
    private TicketTypeStatus status;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private EventDTO event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Integer getMaxPerOrder() {
        return maxPerOrder;
    }

    public void setMaxPerOrder(Integer maxPerOrder) {
        this.maxPerOrder = maxPerOrder;
    }

    public TicketTypeStatus getStatus() {
        return status;
    }

    public void setStatus(TicketTypeStatus status) {
        this.status = status;
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

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketTypeDTO)) {
            return false;
        }

        TicketTypeDTO ticketTypeDTO = (TicketTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ticketTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", currency='" + getCurrency() + "'" +
            ", totalQuantity=" + getTotalQuantity() +
            ", availableQuantity=" + getAvailableQuantity() +
            ", reservedQuantity=" + getReservedQuantity() +
            ", soldQuantity=" + getSoldQuantity() +
            ", maxPerOrder=" + getMaxPerOrder() +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", event=" + getEvent() +
            "}";
    }
}
