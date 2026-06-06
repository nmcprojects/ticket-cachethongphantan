package com.ticketbooking.event.service.mapper;

import com.ticketbooking.event.domain.OrganizerProfile;
import com.ticketbooking.event.service.dto.OrganizerProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganizerProfile} and its DTO {@link OrganizerProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrganizerProfileMapper extends EntityMapper<OrganizerProfileDTO, OrganizerProfile> {}
