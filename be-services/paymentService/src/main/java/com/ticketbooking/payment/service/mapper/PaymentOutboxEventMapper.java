package com.ticketbooking.payment.service.mapper;

import com.ticketbooking.payment.domain.PaymentOutboxEvent;
import com.ticketbooking.payment.service.dto.PaymentOutboxEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentOutboxEvent} and its DTO {@link PaymentOutboxEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentOutboxEventMapper extends EntityMapper<PaymentOutboxEventDTO, PaymentOutboxEvent> {}
