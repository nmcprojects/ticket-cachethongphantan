package com.ticketbooking.booking.repository;

import com.ticketbooking.booking.domain.BookingItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookingItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookingItemRepository extends JpaRepository<BookingItem, Long> {}
