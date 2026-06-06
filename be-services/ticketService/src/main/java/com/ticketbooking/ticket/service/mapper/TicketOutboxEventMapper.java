package com.ticketbooking.ticket.service.mapper;

import com.ticketbooking.ticket.domain.TicketOutboxEvent;
import com.ticketbooking.ticket.service.dto.TicketOutboxEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TicketOutboxEvent} and its DTO {@link TicketOutboxEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface TicketOutboxEventMapper extends EntityMapper<TicketOutboxEventDTO, TicketOutboxEvent> {}
