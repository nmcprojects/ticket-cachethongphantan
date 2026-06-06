package com.ticketbooking.ticket.service.mapper;

import com.ticketbooking.ticket.domain.CheckinLog;
import com.ticketbooking.ticket.domain.Ticket;
import com.ticketbooking.ticket.service.dto.CheckinLogDTO;
import com.ticketbooking.ticket.service.dto.TicketDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CheckinLog} and its DTO {@link CheckinLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface CheckinLogMapper extends EntityMapper<CheckinLogDTO, CheckinLog> {
    @Mapping(target = "ticket", source = "ticket", qualifiedByName = "ticketId")
    CheckinLogDTO toDto(CheckinLog s);

    @Named("ticketId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TicketDTO toDtoTicketId(Ticket ticket);
}
