package com.ticketbooking.payment.service.mapper;

import com.ticketbooking.payment.domain.Payment;
import com.ticketbooking.payment.domain.PaymentAttempt;
import com.ticketbooking.payment.domain.PaymentWebhookLog;
import com.ticketbooking.payment.service.dto.PaymentAttemptDTO;
import com.ticketbooking.payment.service.dto.PaymentDTO;
import com.ticketbooking.payment.service.dto.PaymentWebhookLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentWebhookLog} and its DTO {@link PaymentWebhookLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentWebhookLogMapper extends EntityMapper<PaymentWebhookLogDTO, PaymentWebhookLog> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    @Mapping(target = "attempt", source = "attempt", qualifiedByName = "paymentAttemptId")
    PaymentWebhookLogDTO toDto(PaymentWebhookLog s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);

    @Named("paymentAttemptId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentAttemptDTO toDtoPaymentAttemptId(PaymentAttempt paymentAttempt);
}
