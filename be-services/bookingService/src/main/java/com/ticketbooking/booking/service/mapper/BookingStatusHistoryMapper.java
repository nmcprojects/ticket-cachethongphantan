package com.ticketbooking.booking.service.mapper;

import com.ticketbooking.booking.domain.Booking;
import com.ticketbooking.booking.domain.BookingStatusHistory;
import com.ticketbooking.booking.service.dto.BookingDTO;
import com.ticketbooking.booking.service.dto.BookingStatusHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookingStatusHistory} and its DTO {@link BookingStatusHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingStatusHistoryMapper extends EntityMapper<BookingStatusHistoryDTO, BookingStatusHistory> {
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingId")
    BookingStatusHistoryDTO toDto(BookingStatusHistory s);

    @Named("bookingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BookingDTO toDtoBookingId(Booking booking);
}
