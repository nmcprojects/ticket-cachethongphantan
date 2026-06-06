package com.ticketbooking.event.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ticketbooking.event.domain.enumeration.TicketTypeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Ticket Type (loại vé: Standard, VIP, VVIP…)
 * Inventory rule: totalQuantity = availableQuantity + reservedQuantity + soldQuantity
 */
@Entity
@Table(name = "ticket_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TicketType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Size(max = 10)
    @Column(name = "currency", length = 10, nullable = false)
    private String currency;

    @NotNull
    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @NotNull
    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    @NotNull
    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity;

    @NotNull
    @Column(name = "sold_quantity", nullable = false)
    private Integer soldQuantity;

    @NotNull
    @Column(name = "max_per_order", nullable = false)
    private Integer maxPerOrder;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketTypeStatus status;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticketType")
    @JsonIgnoreProperties(value = { "ticketType" }, allowSetters = true)
    private Set<TicketReservation> reservations = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "ticketTypes", "organizer" }, allowSetters = true)
    private Event event;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TicketType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TicketType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public TicketType description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public TicketType price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return this.currency;
    }

    public TicketType currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getTotalQuantity() {
        return this.totalQuantity;
    }

    public TicketType totalQuantity(Integer totalQuantity) {
        this.setTotalQuantity(totalQuantity);
        return this;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getAvailableQuantity() {
        return this.availableQuantity;
    }

    public TicketType availableQuantity(Integer availableQuantity) {
        this.setAvailableQuantity(availableQuantity);
        return this;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getReservedQuantity() {
        return this.reservedQuantity;
    }

    public TicketType reservedQuantity(Integer reservedQuantity) {
        this.setReservedQuantity(reservedQuantity);
        return this;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Integer getSoldQuantity() {
        return this.soldQuantity;
    }

    public TicketType soldQuantity(Integer soldQuantity) {
        this.setSoldQuantity(soldQuantity);
        return this;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Integer getMaxPerOrder() {
        return this.maxPerOrder;
    }

    public TicketType maxPerOrder(Integer maxPerOrder) {
        this.setMaxPerOrder(maxPerOrder);
        return this;
    }

    public void setMaxPerOrder(Integer maxPerOrder) {
        this.maxPerOrder = maxPerOrder;
    }

    public TicketTypeStatus getStatus() {
        return this.status;
    }

    public TicketType status(TicketTypeStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TicketTypeStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public TicketType createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public TicketType updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<TicketReservation> getReservations() {
        return this.reservations;
    }

    public void setReservations(Set<TicketReservation> ticketReservations) {
        if (this.reservations != null) {
            this.reservations.forEach(i -> i.setTicketType(null));
        }
        if (ticketReservations != null) {
            ticketReservations.forEach(i -> i.setTicketType(this));
        }
        this.reservations = ticketReservations;
    }

    public TicketType reservations(Set<TicketReservation> ticketReservations) {
        this.setReservations(ticketReservations);
        return this;
    }

    public TicketType addReservations(TicketReservation ticketReservation) {
        this.reservations.add(ticketReservation);
        ticketReservation.setTicketType(this);
        return this;
    }

    public TicketType removeReservations(TicketReservation ticketReservation) {
        this.reservations.remove(ticketReservation);
        ticketReservation.setTicketType(null);
        return this;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public TicketType event(Event event) {
        this.setEvent(event);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TicketType)) {
            return false;
        }
        return getId() != null && getId().equals(((TicketType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketType{" +
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
            "}";
    }
}
