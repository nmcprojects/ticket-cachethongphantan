package com.ticketbooking.ticket.service;

import com.ticketbooking.ticket.domain.TicketInboxEvent;
import com.ticketbooking.ticket.repository.TicketInboxEventRepository;
import com.ticketbooking.ticket.service.dto.TicketInboxEventDTO;
import com.ticketbooking.ticket.service.mapper.TicketInboxEventMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.ticket.domain.TicketInboxEvent}.
 */
@Service
@Transactional
public class TicketInboxEventService {

    private static final Logger LOG = LoggerFactory.getLogger(TicketInboxEventService.class);

    private final TicketInboxEventRepository ticketInboxEventRepository;

    private final TicketInboxEventMapper ticketInboxEventMapper;

    public TicketInboxEventService(TicketInboxEventRepository ticketInboxEventRepository, TicketInboxEventMapper ticketInboxEventMapper) {
        this.ticketInboxEventRepository = ticketInboxEventRepository;
        this.ticketInboxEventMapper = ticketInboxEventMapper;
    }

    /**
     * Save a ticketInboxEvent.
     *
     * @param ticketInboxEventDTO the entity to save.
     * @return the persisted entity.
     */
    public TicketInboxEventDTO save(TicketInboxEventDTO ticketInboxEventDTO) {
        LOG.debug("Request to save TicketInboxEvent : {}", ticketInboxEventDTO);
        TicketInboxEvent ticketInboxEvent = ticketInboxEventMapper.toEntity(ticketInboxEventDTO);
        ticketInboxEvent = ticketInboxEventRepository.save(ticketInboxEvent);
        return ticketInboxEventMapper.toDto(ticketInboxEvent);
    }

    /**
     * Update a ticketInboxEvent.
     *
     * @param ticketInboxEventDTO the entity to save.
     * @return the persisted entity.
     */
    public TicketInboxEventDTO update(TicketInboxEventDTO ticketInboxEventDTO) {
        LOG.debug("Request to update TicketInboxEvent : {}", ticketInboxEventDTO);
        TicketInboxEvent ticketInboxEvent = ticketInboxEventMapper.toEntity(ticketInboxEventDTO);
        ticketInboxEvent = ticketInboxEventRepository.save(ticketInboxEvent);
        return ticketInboxEventMapper.toDto(ticketInboxEvent);
    }

    /**
     * Partially update a ticketInboxEvent.
     *
     * @param ticketInboxEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TicketInboxEventDTO> partialUpdate(TicketInboxEventDTO ticketInboxEventDTO) {
        LOG.debug("Request to partially update TicketInboxEvent : {}", ticketInboxEventDTO);

        return ticketInboxEventRepository
            .findById(ticketInboxEventDTO.getId())
            .map(existingTicketInboxEvent -> {
                ticketInboxEventMapper.partialUpdate(existingTicketInboxEvent, ticketInboxEventDTO);

                return existingTicketInboxEvent;
            })
            .map(ticketInboxEventRepository::save)
            .map(ticketInboxEventMapper::toDto);
    }

    /**
     * Get all the ticketInboxEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TicketInboxEventDTO> findAll() {
        LOG.debug("Request to get all TicketInboxEvents");
        return ticketInboxEventRepository
            .findAll()
            .stream()
            .map(ticketInboxEventMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one ticketInboxEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TicketInboxEventDTO> findOne(Long id) {
        LOG.debug("Request to get TicketInboxEvent : {}", id);
        return ticketInboxEventRepository.findById(id).map(ticketInboxEventMapper::toDto);
    }

    /**
     * Delete the ticketInboxEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TicketInboxEvent : {}", id);
        ticketInboxEventRepository.deleteById(id);
    }
}
