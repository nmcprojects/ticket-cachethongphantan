package com.ticketbooking.ticket.repository;

import com.ticketbooking.ticket.domain.CheckinLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CheckinLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckinLogRepository extends JpaRepository<CheckinLog, Long> {}
