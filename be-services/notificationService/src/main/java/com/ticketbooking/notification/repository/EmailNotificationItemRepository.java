package com.ticketbooking.notification.repository;

import com.ticketbooking.notification.domain.EmailNotificationItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmailNotificationItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailNotificationItemRepository extends JpaRepository<EmailNotificationItem, Long> {}
