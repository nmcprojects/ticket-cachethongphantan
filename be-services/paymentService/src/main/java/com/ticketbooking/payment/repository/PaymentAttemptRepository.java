package com.ticketbooking.payment.repository;

import com.ticketbooking.payment.domain.PaymentAttempt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentAttempt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentAttemptRepository extends JpaRepository<PaymentAttempt, Long> {}
