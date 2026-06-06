package com.ticketbooking.payment.repository;

import com.ticketbooking.payment.domain.PaymentOutboxEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentOutboxEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentOutboxEventRepository extends JpaRepository<PaymentOutboxEvent, Long> {}
