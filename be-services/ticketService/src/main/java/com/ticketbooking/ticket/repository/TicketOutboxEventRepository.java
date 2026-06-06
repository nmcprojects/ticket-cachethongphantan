package com.ticketbooking.ticket.repository;

import com.ticketbooking.ticket.domain.TicketOutboxEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TicketOutboxEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketOutboxEventRepository extends JpaRepository<TicketOutboxEvent, Long> {}
