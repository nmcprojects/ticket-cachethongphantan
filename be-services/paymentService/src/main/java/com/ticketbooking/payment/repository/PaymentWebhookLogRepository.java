package com.ticketbooking.payment.repository;

import com.ticketbooking.payment.domain.PaymentWebhookLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentWebhookLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentWebhookLogRepository extends JpaRepository<PaymentWebhookLog, Long> {}
