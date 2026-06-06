package com.ticketbooking.booking.repository;

import com.ticketbooking.booking.domain.BookingStatusHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookingStatusHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookingStatusHistoryRepository extends JpaRepository<BookingStatusHistory, Long> {}
