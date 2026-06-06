package com.ticketbooking.ticket.service.mapper;

import com.ticketbooking.ticket.domain.Ticket;
import com.ticketbooking.ticket.service.dto.TicketDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ticket} and its DTO {@link TicketDTO}.
 */
@Mapper(componentModel = "spring")
public interface TicketMapper extends EntityMapper<TicketDTO, Ticket> {}
