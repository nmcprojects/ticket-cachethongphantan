package com.ticketbooking.ticket.service;

import com.ticketbooking.ticket.domain.TicketOutboxEvent;
import com.ticketbooking.ticket.repository.TicketOutboxEventRepository;
import com.ticketbooking.ticket.service.dto.TicketOutboxEventDTO;
import com.ticketbooking.ticket.service.mapper.TicketOutboxEventMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.ticket.domain.TicketOutboxEvent}.
 */
@Service
@Transactional
public class TicketOutboxEventService {

    private static final Logger LOG = LoggerFactory.getLogger(TicketOutboxEventService.class);

    private final TicketOutboxEventRepository ticketOutboxEventRepository;

    private final TicketOutboxEventMapper ticketOutboxEventMapper;

    public TicketOutboxEventService(
        TicketOutboxEventRepository ticketOutboxEventRepository,
        TicketOutboxEventMapper ticketOutboxEventMapper
    ) {
        this.ticketOutboxEventRepository = ticketOutboxEventRepository;
        this.ticketOutboxEventMapper = ticketOutboxEventMapper;
    }

    /**
     * Save a ticketOutboxEvent.
     *
     * @param ticketOutboxEventDTO the entity to save.
     * @return the persisted entity.
     */
    public TicketOutboxEventDTO save(TicketOutboxEventDTO ticketOutboxEventDTO) {
        LOG.debug("Request to save TicketOutboxEvent : {}", ticketOutboxEventDTO);
        TicketOutboxEvent ticketOutboxEvent = ticketOutboxEventMapper.toEntity(ticketOutboxEventDTO);
        ticketOutboxEvent = ticketOutboxEventRepository.save(ticketOutboxEvent);
        return ticketOutboxEventMapper.toDto(ticketOutboxEvent);
    }

    /**
     * Update a ticketOutboxEvent.
     *
     * @param ticketOutboxEventDTO the entity to save.
     * @return the persisted entity.
     */
    public TicketOutboxEventDTO update(TicketOutboxEventDTO ticketOutboxEventDTO) {
        LOG.debug("Request to update TicketOutboxEvent : {}", ticketOutboxEventDTO);
        TicketOutboxEvent ticketOutboxEvent = ticketOutboxEventMapper.toEntity(ticketOutboxEventDTO);
        ticketOutboxEvent = ticketOutboxEventRepository.save(ticketOutboxEvent);
        return ticketOutboxEventMapper.toDto(ticketOutboxEvent);
    }

    /**
     * Partially update a ticketOutboxEvent.
     *
     * @param ticketOutboxEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TicketOutboxEventDTO> partialUpdate(TicketOutboxEventDTO ticketOutboxEventDTO) {
        LOG.debug("Request to partially update TicketOutboxEvent : {}", ticketOutboxEventDTO);

        return ticketOutboxEventRepository
            .findById(ticketOutboxEventDTO.getId())
            .map(existingTicketOutboxEvent -> {
                ticketOutboxEventMapper.partialUpdate(existingTicketOutboxEvent, ticketOutboxEventDTO);

                return existingTicketOutboxEvent;
            })
            .map(ticketOutboxEventRepository::save)
            .map(ticketOutboxEventMapper::toDto);
    }

    /**
     * Get all the ticketOutboxEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TicketOutboxEventDTO> findAll() {
        LOG.debug("Request to get all TicketOutboxEvents");
        return ticketOutboxEventRepository
            .findAll()
            .stream()
            .map(ticketOutboxEventMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one ticketOutboxEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TicketOutboxEventDTO> findOne(Long id) {
        LOG.debug("Request to get TicketOutboxEvent : {}", id);
        return ticketOutboxEventRepository.findById(id).map(ticketOutboxEventMapper::toDto);
    }

    /**
     * Delete the ticketOutboxEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TicketOutboxEvent : {}", id);
        ticketOutboxEventRepository.deleteById(id);
    }
}
