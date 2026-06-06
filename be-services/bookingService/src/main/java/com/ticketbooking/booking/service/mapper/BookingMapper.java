package com.ticketbooking.booking.service.mapper;

import com.ticketbooking.booking.domain.Booking;
import com.ticketbooking.booking.service.dto.BookingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {}
