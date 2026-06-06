package com.ticketbooking.ticket.repository;

import com.ticketbooking.ticket.domain.TicketInboxEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TicketInboxEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketInboxEventRepository extends JpaRepository<TicketInboxEvent, Long> {}
