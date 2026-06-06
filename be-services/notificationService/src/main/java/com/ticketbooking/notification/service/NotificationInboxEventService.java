package com.ticketbooking.notification.service;

import com.ticketbooking.notification.domain.NotificationInboxEvent;
import com.ticketbooking.notification.repository.NotificationInboxEventRepository;
import com.ticketbooking.notification.service.dto.NotificationInboxEventDTO;
import com.ticketbooking.notification.service.mapper.NotificationInboxEventMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.notification.domain.NotificationInboxEvent}.
 */
@Service
@Transactional
public class NotificationInboxEventService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationInboxEventService.class);

    private final NotificationInboxEventRepository notificationInboxEventRepository;

    private final NotificationInboxEventMapper notificationInboxEventMapper;

    public NotificationInboxEventService(
        NotificationInboxEventRepository notificationInboxEventRepository,
        NotificationInboxEventMapper notificationInboxEventMapper
    ) {
        this.notificationInboxEventRepository = notificationInboxEventRepository;
        this.notificationInboxEventMapper = notificationInboxEventMapper;
    }

    /**
     * Save a notificationInboxEvent.
     *
     * @param notificationInboxEventDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationInboxEventDTO save(NotificationInboxEventDTO notificationInboxEventDTO) {
        LOG.debug("Request to save NotificationInboxEvent : {}", notificationInboxEventDTO);
        NotificationInboxEvent notificationInboxEvent = notificationInboxEventMapper.toEntity(notificationInboxEventDTO);
        notificationInboxEvent = notificationInboxEventRepository.save(notificationInboxEvent);
        return notificationInboxEventMapper.toDto(notificationInboxEvent);
    }

    /**
     * Update a notificationInboxEvent.
     *
     * @param notificationInboxEventDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationInboxEventDTO update(NotificationInboxEventDTO notificationInboxEventDTO) {
        LOG.debug("Request to update NotificationInboxEvent : {}", notificationInboxEventDTO);
        NotificationInboxEvent notificationInboxEvent = notificationInboxEventMapper.toEntity(notificationInboxEventDTO);
        notificationInboxEvent = notificationInboxEventRepository.save(notificationInboxEvent);
        return notificationInboxEventMapper.toDto(notificationInboxEvent);
    }

    /**
     * Partially update a notificationInboxEvent.
     *
     * @param notificationInboxEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NotificationInboxEventDTO> partialUpdate(NotificationInboxEventDTO notificationInboxEventDTO) {
        LOG.debug("Request to partially update NotificationInboxEvent : {}", notificationInboxEventDTO);

        return notificationInboxEventRepository
            .findById(notificationInboxEventDTO.getId())
            .map(existingNotificationInboxEvent -> {
                notificationInboxEventMapper.partialUpdate(existingNotificationInboxEvent, notificationInboxEventDTO);

                return existingNotificationInboxEvent;
            })
            .map(notificationInboxEventRepository::save)
            .map(notificationInboxEventMapper::toDto);
    }

    /**
     * Get all the notificationInboxEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NotificationInboxEventDTO> findAll() {
        LOG.debug("Request to get all NotificationInboxEvents");
        return notificationInboxEventRepository
            .findAll()
            .stream()
            .map(notificationInboxEventMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one notificationInboxEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationInboxEventDTO> findOne(Long id) {
        LOG.debug("Request to get NotificationInboxEvent : {}", id);
        return notificationInboxEventRepository.findById(id).map(notificationInboxEventMapper::toDto);
    }

    /**
     * Delete the notificationInboxEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NotificationInboxEvent : {}", id);
        notificationInboxEventRepository.deleteById(id);
    }
}
