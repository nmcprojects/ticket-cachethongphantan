package com.ticketbooking.payment.service.mapper;

import com.ticketbooking.payment.domain.Payment;
import com.ticketbooking.payment.domain.PaymentAttempt;
import com.ticketbooking.payment.service.dto.PaymentAttemptDTO;
import com.ticketbooking.payment.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentAttempt} and its DTO {@link PaymentAttemptDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentAttemptMapper extends EntityMapper<PaymentAttemptDTO, PaymentAttempt> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    PaymentAttemptDTO toDto(PaymentAttempt s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);
}
