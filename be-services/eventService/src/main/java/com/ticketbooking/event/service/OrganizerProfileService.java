package com.ticketbooking.event.service;

import com.ticketbooking.event.domain.OrganizerProfile;
import com.ticketbooking.event.repository.OrganizerProfileRepository;
import com.ticketbooking.event.service.dto.OrganizerProfileDTO;
import com.ticketbooking.event.service.mapper.OrganizerProfileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.event.domain.OrganizerProfile}.
 */
@Service
@Transactional
public class OrganizerProfileService {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizerProfileService.class);

    private final OrganizerProfileRepository organizerProfileRepository;

    private final OrganizerProfileMapper organizerProfileMapper;

    public OrganizerProfileService(OrganizerProfileRepository organizerProfileRepository, OrganizerProfileMapper organizerProfileMapper) {
        this.organizerProfileRepository = organizerProfileRepository;
        this.organizerProfileMapper = organizerProfileMapper;
    }

    /**
     * Save a organizerProfile.
     *
     * @param organizerProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public OrganizerProfileDTO save(OrganizerProfileDTO organizerProfileDTO) {
        LOG.debug("Request to save OrganizerProfile : {}", organizerProfileDTO);
        OrganizerProfile organizerProfile = organizerProfileMapper.toEntity(organizerProfileDTO);
        organizerProfile = organizerProfileRepository.save(organizerProfile);
        return organizerProfileMapper.toDto(organizerProfile);
    }

    /**
     * Update a organizerProfile.
     *
     * @param organizerProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public OrganizerProfileDTO update(OrganizerProfileDTO organizerProfileDTO) {
        LOG.debug("Request to update OrganizerProfile : {}", organizerProfileDTO);
        OrganizerProfile organizerProfile = organizerProfileMapper.toEntity(organizerProfileDTO);
        organizerProfile = organizerProfileRepository.save(organizerProfile);
        return organizerProfileMapper.toDto(organizerProfile);
    }

    /**
     * Partially update a organizerProfile.
     *
     * @param organizerProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrganizerProfileDTO> partialUpdate(OrganizerProfileDTO organizerProfileDTO) {
        LOG.debug("Request to partially update OrganizerProfile : {}", organizerProfileDTO);

        return organizerProfileRepository
            .findById(organizerProfileDTO.getId())
            .map(existingOrganizerProfile -> {
                organizerProfileMapper.partialUpdate(existingOrganizerProfile, organizerProfileDTO);

                return existingOrganizerProfile;
            })
            .map(organizerProfileRepository::save)
            .map(organizerProfileMapper::toDto);
    }

    /**
     * Get all the organizerProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganizerProfileDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all OrganizerProfiles");
        return organizerProfileRepository.findAll(pageable).map(organizerProfileMapper::toDto);
    }

    /**
     * Get one organizerProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrganizerProfileDTO> findOne(Long id) {
        LOG.debug("Request to get OrganizerProfile : {}", id);
        return organizerProfileRepository.findById(id).map(organizerProfileMapper::toDto);
    }

    /**
     * Delete the organizerProfile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrganizerProfile : {}", id);
        organizerProfileRepository.deleteById(id);
    }
}
