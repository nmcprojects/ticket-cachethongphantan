package com.ticketbooking.notification.service;

import com.ticketbooking.notification.domain.EmailNotification;
import com.ticketbooking.notification.repository.EmailNotificationRepository;
import com.ticketbooking.notification.service.dto.EmailNotificationDTO;
import com.ticketbooking.notification.service.mapper.EmailNotificationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.notification.domain.EmailNotification}.
 */
@Service
@Transactional
public class EmailNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailNotificationService.class);

    private final EmailNotificationRepository emailNotificationRepository;

    private final EmailNotificationMapper emailNotificationMapper;

    public EmailNotificationService(
        EmailNotificationRepository emailNotificationRepository,
        EmailNotificationMapper emailNotificationMapper
    ) {
        this.emailNotificationRepository = emailNotificationRepository;
        this.emailNotificationMapper = emailNotificationMapper;
    }

    /**
     * Save a emailNotification.
     *
     * @param emailNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailNotificationDTO save(EmailNotificationDTO emailNotificationDTO) {
        LOG.debug("Request to save EmailNotification : {}", emailNotificationDTO);
        EmailNotification emailNotification = emailNotificationMapper.toEntity(emailNotificationDTO);
        emailNotification = emailNotificationRepository.save(emailNotification);
        return emailNotificationMapper.toDto(emailNotification);
    }

    /**
     * Update a emailNotification.
     *
     * @param emailNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailNotificationDTO update(EmailNotificationDTO emailNotificationDTO) {
        LOG.debug("Request to update EmailNotification : {}", emailNotificationDTO);
        EmailNotification emailNotification = emailNotificationMapper.toEntity(emailNotificationDTO);
        emailNotification = emailNotificationRepository.save(emailNotification);
        return emailNotificationMapper.toDto(emailNotification);
    }

    /**
     * Partially update a emailNotification.
     *
     * @param emailNotificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmailNotificationDTO> partialUpdate(EmailNotificationDTO emailNotificationDTO) {
        LOG.debug("Request to partially update EmailNotification : {}", emailNotificationDTO);

        return emailNotificationRepository
            .findById(emailNotificationDTO.getId())
            .map(existingEmailNotification -> {
                emailNotificationMapper.partialUpdate(existingEmailNotification, emailNotificationDTO);

                return existingEmailNotification;
            })
            .map(emailNotificationRepository::save)
            .map(emailNotificationMapper::toDto);
    }

    /**
     * Get all the emailNotifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EmailNotificationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all EmailNotifications");
        return emailNotificationRepository.findAll(pageable).map(emailNotificationMapper::toDto);
    }

    /**
     * Get one emailNotification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmailNotificationDTO> findOne(Long id) {
        LOG.debug("Request to get EmailNotification : {}", id);
        return emailNotificationRepository.findById(id).map(emailNotificationMapper::toDto);
    }

    /**
     * Delete the emailNotification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmailNotification : {}", id);
        emailNotificationRepository.deleteById(id);
    }
}
