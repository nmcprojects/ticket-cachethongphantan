package com.ticketbooking.booking.service.mapper;

import com.ticketbooking.booking.domain.Booking;
import com.ticketbooking.booking.domain.BookingItem;
import com.ticketbooking.booking.service.dto.BookingDTO;
import com.ticketbooking.booking.service.dto.BookingItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BookingItem} and its DTO {@link BookingItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingItemMapper extends EntityMapper<BookingItemDTO, BookingItem> {
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingId")
    BookingItemDTO toDto(BookingItem s);

    @Named("bookingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BookingDTO toDtoBookingId(Booking booking);
}
