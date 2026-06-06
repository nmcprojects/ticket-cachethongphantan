package com.ticketbooking.booking.service.mapper;

import com.ticketbooking.booking.domain.BookingInboxEvent;
import com.ticketbooking.booking.service.dto.BookingInboxEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookingInboxEvent} and its DTO {@link BookingInboxEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingInboxEventMapper extends EntityMapper<BookingInboxEventDTO, BookingInboxEvent> {}
