package com.ticketbooking.event.service;

import com.ticketbooking.event.domain.TicketReservation;
import com.ticketbooking.event.repository.TicketReservationRepository;
import com.ticketbooking.event.service.dto.TicketReservationDTO;
import com.ticketbooking.event.service.mapper.TicketReservationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.event.domain.TicketReservation}.
 */
@Service
@Transactional
public class TicketReservationService {

    private static final Logger LOG = LoggerFactory.getLogger(TicketReservationService.class);

    private final TicketReservationRepository ticketReservationRepository;

    private final TicketReservationMapper ticketReservationMapper;

    public TicketReservationService(
        TicketReservationRepository ticketReservationRepository,
        TicketReservationMapper ticketReservationMapper
    ) {
        this.ticketReservationRepository = ticketReservationRepository;
        this.ticketReservationMapper = ticketReservationMapper;
    }

    /**
     * Save a ticketReservation.
     *
     * @param ticketReservationDTO the entity to save.
     * @return the persisted entity.
     */
    public TicketReservationDTO save(TicketReservationDTO ticketReservationDTO) {
        LOG.debug("Request to save TicketReservation : {}", ticketReservationDTO);
        TicketReservation ticketReservation = ticketReservationMapper.toEntity(ticketReservationDTO);
        ticketReservation = ticketReservationRepository.save(ticketReservation);
        return ticketReservationMapper.toDto(ticketReservation);
    }

    /**
     * Update a ticketReservation.
     *
     * @param ticketReservationDTO the entity to save.
     * @return the persisted entity.
     */
    public TicketReservationDTO update(TicketReservationDTO ticketReservationDTO) {
        LOG.debug("Request to update TicketReservation : {}", ticketReservationDTO);
        TicketReservation ticketReservation = ticketReservationMapper.toEntity(ticketReservationDTO);
        ticketReservation = ticketReservationRepository.save(ticketReservation);
        return ticketReservationMapper.toDto(ticketReservation);
    }

    /**
     * Partially update a ticketReservation.
     *
     * @param ticketReservationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TicketReservationDTO> partialUpdate(TicketReservationDTO ticketReservationDTO) {
        LOG.debug("Request to partially update TicketReservation : {}", ticketReservationDTO);

        return ticketReservationRepository
            .findById(ticketReservationDTO.getId())
            .map(existingTicketReservation -> {
                ticketReservationMapper.partialUpdate(existingTicketReservation, ticketReservationDTO);

                return existingTicketReservation;
            })
            .map(ticketReservationRepository::save)
            .map(ticketReservationMapper::toDto);
    }

    /**
     * Get all the ticketReservations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TicketReservationDTO> findAll() {
        LOG.debug("Request to get all TicketReservations");
        return ticketReservationRepository
            .findAll()
            .stream()
            .map(ticketReservationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one ticketReservation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TicketReservationDTO> findOne(Long id) {
        LOG.debug("Request to get TicketReservation : {}", id);
        return ticketReservationRepository.findById(id).map(ticketReservationMapper::toDto);
    }

    /**
     * Delete the ticketReservation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TicketReservation : {}", id);
        ticketReservationRepository.deleteById(id);
    }
}
