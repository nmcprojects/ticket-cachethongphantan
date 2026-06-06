package com.ticketbooking.event.repository;

import com.ticketbooking.event.domain.TicketType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TicketType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {}
