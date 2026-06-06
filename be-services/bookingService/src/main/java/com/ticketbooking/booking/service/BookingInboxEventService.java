package com.ticketbooking.booking.service;

import com.ticketbooking.booking.domain.BookingInboxEvent;
import com.ticketbooking.booking.repository.BookingInboxEventRepository;
import com.ticketbooking.booking.service.dto.BookingInboxEventDTO;
import com.ticketbooking.booking.service.mapper.BookingInboxEventMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.booking.domain.BookingInboxEvent}.
 */
@Service
@Transactional
public class BookingInboxEventService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingInboxEventService.class);

    private final BookingInboxEventRepository bookingInboxEventRepository;

    private final BookingInboxEventMapper bookingInboxEventMapper;

    public BookingInboxEventService(
        BookingInboxEventRepository bookingInboxEventRepository,
        BookingInboxEventMapper bookingInboxEventMapper
    ) {
        this.bookingInboxEventRepository = bookingInboxEventRepository;
        this.bookingInboxEventMapper = bookingInboxEventMapper;
    }

    /**
     * Save a bookingInboxEvent.
     *
     * @param bookingInboxEventDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingInboxEventDTO save(BookingInboxEventDTO bookingInboxEventDTO) {
        LOG.debug("Request to save BookingInboxEvent : {}", bookingInboxEventDTO);
        BookingInboxEvent bookingInboxEvent = bookingInboxEventMapper.toEntity(bookingInboxEventDTO);
        bookingInboxEvent = bookingInboxEventRepository.save(bookingInboxEvent);
        return bookingInboxEventMapper.toDto(bookingInboxEvent);
    }

    /**
     * Update a bookingInboxEvent.
     *
     * @param bookingInboxEventDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingInboxEventDTO update(BookingInboxEventDTO bookingInboxEventDTO) {
        LOG.debug("Request to update BookingInboxEvent : {}", bookingInboxEventDTO);
        BookingInboxEvent bookingInboxEvent = bookingInboxEventMapper.toEntity(bookingInboxEventDTO);
        bookingInboxEvent = bookingInboxEventRepository.save(bookingInboxEvent);
        return bookingInboxEventMapper.toDto(bookingInboxEvent);
    }

    /**
     * Partially update a bookingInboxEvent.
     *
     * @param bookingInboxEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookingInboxEventDTO> partialUpdate(BookingInboxEventDTO bookingInboxEventDTO) {
        LOG.debug("Request to partially update BookingInboxEvent : {}", bookingInboxEventDTO);

        return bookingInboxEventRepository
            .findById(bookingInboxEventDTO.getId())
            .map(existingBookingInboxEvent -> {
                bookingInboxEventMapper.partialUpdate(existingBookingInboxEvent, bookingInboxEventDTO);

                return existingBookingInboxEvent;
            })
            .map(bookingInboxEventRepository::save)
            .map(bookingInboxEventMapper::toDto);
    }

    /**
     * Get all the bookingInboxEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BookingInboxEventDTO> findAll() {
        LOG.debug("Request to get all BookingInboxEvents");
        return bookingInboxEventRepository
            .findAll()
            .stream()
            .map(bookingInboxEventMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one bookingInboxEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookingInboxEventDTO> findOne(Long id) {
        LOG.debug("Request to get BookingInboxEvent : {}", id);
        return bookingInboxEventRepository.findById(id).map(bookingInboxEventMapper::toDto);
    }

    /**
     * Delete the bookingInboxEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BookingInboxEvent : {}", id);
        bookingInboxEventRepository.deleteById(id);
    }
}
