package com.ticketbooking.booking.repository;

import com.ticketbooking.booking.domain.BookingInboxEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookingInboxEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookingInboxEventRepository extends JpaRepository<BookingInboxEvent, Long> {}
