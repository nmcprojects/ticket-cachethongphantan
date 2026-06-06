package com.ticketbooking.event.repository;

import com.ticketbooking.event.domain.OrganizerProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrganizerProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizerProfileRepository extends JpaRepository<OrganizerProfile, Long> {}
