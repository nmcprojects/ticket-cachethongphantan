package com.ticketbooking.payment.service.mapper;

import com.ticketbooking.payment.domain.Payment;
import com.ticketbooking.payment.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {}
