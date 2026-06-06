package com.ticketbooking.event.service.mapper;

import com.ticketbooking.event.domain.Event;
import com.ticketbooking.event.domain.TicketType;
import com.ticketbooking.event.service.dto.EventDTO;
import com.ticketbooking.event.service.dto.TicketTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TicketType} and its DTO {@link TicketTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface TicketTypeMapper extends EntityMapper<TicketTypeDTO, TicketType> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventId")
    TicketTypeDTO toDto(TicketType s);

    @Named("eventId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDTO toDtoEventId(Event event);
}
