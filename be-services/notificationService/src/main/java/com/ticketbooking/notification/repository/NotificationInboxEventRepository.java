package com.ticketbooking.notification.repository;

import com.ticketbooking.notification.domain.NotificationInboxEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NotificationInboxEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationInboxEventRepository extends JpaRepository<NotificationInboxEvent, Long> {}
