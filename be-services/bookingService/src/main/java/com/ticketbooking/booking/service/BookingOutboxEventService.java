package com.ticketbooking.booking.service;

import com.ticketbooking.booking.domain.BookingOutboxEvent;
import com.ticketbooking.booking.repository.BookingOutboxEventRepository;
import com.ticketbooking.booking.service.dto.BookingOutboxEventDTO;
import com.ticketbooking.booking.service.mapper.BookingOutboxEventMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.booking.domain.BookingOutboxEvent}.
 */
@Service
@Transactional
public class BookingOutboxEventService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingOutboxEventService.class);

    private final BookingOutboxEventRepository bookingOutboxEventRepository;

    private final BookingOutboxEventMapper bookingOutboxEventMapper;

    public BookingOutboxEventService(
        BookingOutboxEventRepository bookingOutboxEventRepository,
        BookingOutboxEventMapper bookingOutboxEventMapper
    ) {
        this.bookingOutboxEventRepository = bookingOutboxEventRepository;
        this.bookingOutboxEventMapper = bookingOutboxEventMapper;
    }

    /**
     * Save a bookingOutboxEvent.
     *
     * @param bookingOutboxEventDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingOutboxEventDTO save(BookingOutboxEventDTO bookingOutboxEventDTO) {
        LOG.debug("Request to save BookingOutboxEvent : {}", bookingOutboxEventDTO);
        BookingOutboxEvent bookingOutboxEvent = bookingOutboxEventMapper.toEntity(bookingOutboxEventDTO);
        bookingOutboxEvent = bookingOutboxEventRepository.save(bookingOutboxEvent);
        return bookingOutboxEventMapper.toDto(bookingOutboxEvent);
    }

    /**
     * Update a bookingOutboxEvent.
     *
     * @param bookingOutboxEventDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingOutboxEventDTO update(BookingOutboxEventDTO bookingOutboxEventDTO) {
        LOG.debug("Request to update BookingOutboxEvent : {}", bookingOutboxEventDTO);
        BookingOutboxEvent bookingOutboxEvent = bookingOutboxEventMapper.toEntity(bookingOutboxEventDTO);
        bookingOutboxEvent = bookingOutboxEventRepository.save(bookingOutboxEvent);
        return bookingOutboxEventMapper.toDto(bookingOutboxEvent);
    }

    /**
     * Partially update a bookingOutboxEvent.
     *
     * @param bookingOutboxEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookingOutboxEventDTO> partialUpdate(BookingOutboxEventDTO bookingOutboxEventDTO) {
        LOG.debug("Request to partially update BookingOutboxEvent : {}", bookingOutboxEventDTO);

        return bookingOutboxEventRepository
            .findById(bookingOutboxEventDTO.getId())
            .map(existingBookingOutboxEvent -> {
                bookingOutboxEventMapper.partialUpdate(existingBookingOutboxEvent, bookingOutboxEventDTO);

                return existingBookingOutboxEvent;
            })
            .map(bookingOutboxEventRepository::save)
            .map(bookingOutboxEventMapper::toDto);
    }

    /**
     * Get all the bookingOutboxEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BookingOutboxEventDTO> findAll() {
        LOG.debug("Request to get all BookingOutboxEvents");
        return bookingOutboxEventRepository
            .findAll()
            .stream()
            .map(bookingOutboxEventMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one bookingOutboxEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookingOutboxEventDTO> findOne(Long id) {
        LOG.debug("Request to get BookingOutboxEvent : {}", id);
        return bookingOutboxEventRepository.findById(id).map(bookingOutboxEventMapper::toDto);
    }

    /**
     * Delete the bookingOutboxEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BookingOutboxEvent : {}", id);
        bookingOutboxEventRepository.deleteById(id);
    }
}
