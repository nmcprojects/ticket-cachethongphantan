package com.ticketbooking.notification.repository;

import com.ticketbooking.notification.domain.EmailNotification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmailNotification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {}
