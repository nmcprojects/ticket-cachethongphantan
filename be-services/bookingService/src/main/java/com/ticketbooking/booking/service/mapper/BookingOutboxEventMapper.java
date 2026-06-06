package com.ticketbooking.booking.service.mapper;

import com.ticketbooking.booking.domain.BookingOutboxEvent;
import com.ticketbooking.booking.service.dto.BookingOutboxEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookingOutboxEvent} and its DTO {@link BookingOutboxEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingOutboxEventMapper extends EntityMapper<BookingOutboxEventDTO, BookingOutboxEvent> {}
