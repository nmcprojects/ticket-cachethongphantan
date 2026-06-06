package com.ticketbooking.event.service.mapper;

import com.ticketbooking.event.domain.Event;
import com.ticketbooking.event.domain.OrganizerProfile;
import com.ticketbooking.event.service.dto.EventDTO;
import com.ticketbooking.event.service.dto.OrganizerProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventMapper extends EntityMapper<EventDTO, Event> {
    @Mapping(target = "organizer", source = "organizer", qualifiedByName = "organizerProfileId")
    EventDTO toDto(Event s);

    @Named("organizerProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrganizerProfileDTO toDtoOrganizerProfileId(OrganizerProfile organizerProfile);
}
