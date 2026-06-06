package com.ticketbooking.booking.service;

import com.ticketbooking.booking.domain.BookingStatusHistory;
import com.ticketbooking.booking.repository.BookingStatusHistoryRepository;
import com.ticketbooking.booking.service.dto.BookingStatusHistoryDTO;
import com.ticketbooking.booking.service.mapper.BookingStatusHistoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.booking.domain.BookingStatusHistory}.
 */
@Service
@Transactional
public class BookingStatusHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingStatusHistoryService.class);

    private final BookingStatusHistoryRepository bookingStatusHistoryRepository;

    private final BookingStatusHistoryMapper bookingStatusHistoryMapper;

    public BookingStatusHistoryService(
        BookingStatusHistoryRepository bookingStatusHistoryRepository,
        BookingStatusHistoryMapper bookingStatusHistoryMapper
    ) {
        this.bookingStatusHistoryRepository = bookingStatusHistoryRepository;
        this.bookingStatusHistoryMapper = bookingStatusHistoryMapper;
    }

    /**
     * Save a bookingStatusHistory.
     *
     * @param bookingStatusHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingStatusHistoryDTO save(BookingStatusHistoryDTO bookingStatusHistoryDTO) {
        LOG.debug("Request to save BookingStatusHistory : {}", bookingStatusHistoryDTO);
        BookingStatusHistory bookingStatusHistory = bookingStatusHistoryMapper.toEntity(bookingStatusHistoryDTO);
        bookingStatusHistory = bookingStatusHistoryRepository.save(bookingStatusHistory);
        return bookingStatusHistoryMapper.toDto(bookingStatusHistory);
    }

    /**
     * Update a bookingStatusHistory.
     *
     * @param bookingStatusHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingStatusHistoryDTO update(BookingStatusHistoryDTO bookingStatusHistoryDTO) {
        LOG.debug("Request to update BookingStatusHistory : {}", bookingStatusHistoryDTO);
        BookingStatusHistory bookingStatusHistory = bookingStatusHistoryMapper.toEntity(bookingStatusHistoryDTO);
        bookingStatusHistory = bookingStatusHistoryRepository.save(bookingStatusHistory);
        return bookingStatusHistoryMapper.toDto(bookingStatusHistory);
    }

    /**
     * Partially update a bookingStatusHistory.
     *
     * @param bookingStatusHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookingStatusHistoryDTO> partialUpdate(BookingStatusHistoryDTO bookingStatusHistoryDTO) {
        LOG.debug("Request to partially update BookingStatusHistory : {}", bookingStatusHistoryDTO);

        return bookingStatusHistoryRepository
            .findById(bookingStatusHistoryDTO.getId())
            .map(existingBookingStatusHistory -> {
                bookingStatusHistoryMapper.partialUpdate(existingBookingStatusHistory, bookingStatusHistoryDTO);

                return existingBookingStatusHistory;
            })
            .map(bookingStatusHistoryRepository::save)
            .map(bookingStatusHistoryMapper::toDto);
    }

    /**
     * Get all the bookingStatusHistories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BookingStatusHistoryDTO> findAll() {
        LOG.debug("Request to get all BookingStatusHistories");
        return bookingStatusHistoryRepository
            .findAll()
            .stream()
            .map(bookingStatusHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one bookingStatusHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookingStatusHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get BookingStatusHistory : {}", id);
        return bookingStatusHistoryRepository.findById(id).map(bookingStatusHistoryMapper::toDto);
    }

    /**
     * Delete the bookingStatusHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BookingStatusHistory : {}", id);
        bookingStatusHistoryRepository.deleteById(id);
    }
}
