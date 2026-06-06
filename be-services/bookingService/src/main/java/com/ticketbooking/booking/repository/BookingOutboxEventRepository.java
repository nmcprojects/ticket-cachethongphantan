package com.ticketbooking.booking.repository;

import com.ticketbooking.booking.domain.BookingOutboxEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookingOutboxEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookingOutboxEventRepository extends JpaRepository<BookingOutboxEvent, Long> {}
