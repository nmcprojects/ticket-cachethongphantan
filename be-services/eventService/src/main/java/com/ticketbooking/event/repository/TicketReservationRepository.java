package com.ticketbooking.event.repository;

import com.ticketbooking.event.domain.TicketReservation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TicketReservation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketReservationRepository extends JpaRepository<TicketReservation, Long> {}
