package com.ticketbooking.event.service;

import com.ticketbooking.event.domain.TicketType;
import com.ticketbooking.event.repository.TicketTypeRepository;
import com.ticketbooking.event.service.dto.TicketTypeDTO;
import com.ticketbooking.event.service.mapper.TicketTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.event.domain.TicketType}.
 */
@Service
@Transactional
public class TicketTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(TicketTypeService.class);

    private final TicketTypeRepository ticketTypeRepository;

    private final TicketTypeMapper ticketTypeMapper;

    public TicketTypeService(TicketTypeRepository ticketTypeRepository, TicketTypeMapper ticketTypeMapper) {
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketTypeMapper = ticketTypeMapper;
    }

    /**
     * Save a ticketType.
     *
     * @param ticketTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public TicketTypeDTO save(TicketTypeDTO ticketTypeDTO) {
        LOG.debug("Request to save TicketType : {}", ticketTypeDTO);
        TicketType ticketType = ticketTypeMapper.toEntity(ticketTypeDTO);
        ticketType = ticketTypeRepository.save(ticketType);
        return ticketTypeMapper.toDto(ticketType);
    }

    /**
     * Update a ticketType.
     *
     * @param ticketTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public TicketTypeDTO update(TicketTypeDTO ticketTypeDTO) {
        LOG.debug("Request to update TicketType : {}", ticketTypeDTO);
        TicketType ticketType = ticketTypeMapper.toEntity(ticketTypeDTO);
        ticketType = ticketTypeRepository.save(ticketType);
        return ticketTypeMapper.toDto(ticketType);
    }

    /**
     * Partially update a ticketType.
     *
     * @param ticketTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TicketTypeDTO> partialUpdate(TicketTypeDTO ticketTypeDTO) {
        LOG.debug("Request to partially update TicketType : {}", ticketTypeDTO);

        return ticketTypeRepository
            .findById(ticketTypeDTO.getId())
            .map(existingTicketType -> {
                ticketTypeMapper.partialUpdate(existingTicketType, ticketTypeDTO);

                return existingTicketType;
            })
            .map(ticketTypeRepository::save)
            .map(ticketTypeMapper::toDto);
    }

    /**
     * Get all the ticketTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TicketTypeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TicketTypes");
        return ticketTypeRepository.findAll(pageable).map(ticketTypeMapper::toDto);
    }

    /**
     * Get one ticketType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TicketTypeDTO> findOne(Long id) {
        LOG.debug("Request to get TicketType : {}", id);
        return ticketTypeRepository.findById(id).map(ticketTypeMapper::toDto);
    }

    /**
     * Delete the ticketType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TicketType : {}", id);
        ticketTypeRepository.deleteById(id);
    }
}
