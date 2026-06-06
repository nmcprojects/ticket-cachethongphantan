package com.ticketbooking.event.service.mapper;

import com.ticketbooking.event.domain.TicketReservation;
import com.ticketbooking.event.domain.TicketType;
import com.ticketbooking.event.service.dto.TicketReservationDTO;
import com.ticketbooking.event.service.dto.TicketTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TicketReservation} and its DTO {@link TicketReservationDTO}.
 */
@Mapper(componentModel = "spring")
public interface TicketReservationMapper extends EntityMapper<TicketReservationDTO, TicketReservation> {
    @Mapping(target = "ticketType", source = "ticketType", qualifiedByName = "ticketTypeId")
    TicketReservationDTO toDto(TicketReservation s);

    @Named("ticketTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TicketTypeDTO toDtoTicketTypeId(TicketType ticketType);
}
