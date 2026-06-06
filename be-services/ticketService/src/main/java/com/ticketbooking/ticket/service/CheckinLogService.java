package com.ticketbooking.ticket.service;

import com.ticketbooking.ticket.domain.CheckinLog;
import com.ticketbooking.ticket.repository.CheckinLogRepository;
import com.ticketbooking.ticket.service.dto.CheckinLogDTO;
import com.ticketbooking.ticket.service.mapper.CheckinLogMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.ticket.domain.CheckinLog}.
 */
@Service
@Transactional
public class CheckinLogService {

    private static final Logger LOG = LoggerFactory.getLogger(CheckinLogService.class);

    private final CheckinLogRepository checkinLogRepository;

    private final CheckinLogMapper checkinLogMapper;

    public CheckinLogService(CheckinLogRepository checkinLogRepository, CheckinLogMapper checkinLogMapper) {
        this.checkinLogRepository = checkinLogRepository;
        this.checkinLogMapper = checkinLogMapper;
    }

    /**
     * Save a checkinLog.
     *
     * @param checkinLogDTO the entity to save.
     * @return the persisted entity.
     */
    public CheckinLogDTO save(CheckinLogDTO checkinLogDTO) {
        LOG.debug("Request to save CheckinLog : {}", checkinLogDTO);
        CheckinLog checkinLog = checkinLogMapper.toEntity(checkinLogDTO);
        checkinLog = checkinLogRepository.save(checkinLog);
        return checkinLogMapper.toDto(checkinLog);
    }

    /**
     * Update a checkinLog.
     *
     * @param checkinLogDTO the entity to save.
     * @return the persisted entity.
     */
    public CheckinLogDTO update(CheckinLogDTO checkinLogDTO) {
        LOG.debug("Request to update CheckinLog : {}", checkinLogDTO);
        CheckinLog checkinLog = checkinLogMapper.toEntity(checkinLogDTO);
        checkinLog = checkinLogRepository.save(checkinLog);
        return checkinLogMapper.toDto(checkinLog);
    }

    /**
     * Partially update a checkinLog.
     *
     * @param checkinLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CheckinLogDTO> partialUpdate(CheckinLogDTO checkinLogDTO) {
        LOG.debug("Request to partially update CheckinLog : {}", checkinLogDTO);

        return checkinLogRepository
            .findById(checkinLogDTO.getId())
            .map(existingCheckinLog -> {
                checkinLogMapper.partialUpdate(existingCheckinLog, checkinLogDTO);

                return existingCheckinLog;
            })
            .map(checkinLogRepository::save)
            .map(checkinLogMapper::toDto);
    }

    /**
     * Get all the checkinLogs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CheckinLogDTO> findAll() {
        LOG.debug("Request to get all CheckinLogs");
        return checkinLogRepository.findAll().stream().map(checkinLogMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one checkinLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CheckinLogDTO> findOne(Long id) {
        LOG.debug("Request to get CheckinLog : {}", id);
        return checkinLogRepository.findById(id).map(checkinLogMapper::toDto);
    }

    /**
     * Delete the checkinLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CheckinLog : {}", id);
        checkinLogRepository.deleteById(id);
    }
}
