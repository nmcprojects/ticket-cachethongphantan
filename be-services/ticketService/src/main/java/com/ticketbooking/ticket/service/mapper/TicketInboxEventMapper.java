package com.ticketbooking.ticket.service.mapper;

import com.ticketbooking.ticket.domain.TicketInboxEvent;
import com.ticketbooking.ticket.service.dto.TicketInboxEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TicketInboxEvent} and its DTO {@link TicketInboxEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface TicketInboxEventMapper extends EntityMapper<TicketInboxEventDTO, TicketInboxEvent> {}
