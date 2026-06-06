package com.ticketbooking.notification.service;

import com.ticketbooking.notification.domain.EmailNotificationItem;
import com.ticketbooking.notification.repository.EmailNotificationItemRepository;
import com.ticketbooking.notification.service.dto.EmailNotificationItemDTO;
import com.ticketbooking.notification.service.mapper.EmailNotificationItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.notification.domain.EmailNotificationItem}.
 */
@Service
@Transactional
public class EmailNotificationItemService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailNotificationItemService.class);

    private final EmailNotificationItemRepository emailNotificationItemRepository;

    private final EmailNotificationItemMapper emailNotificationItemMapper;

    public EmailNotificationItemService(
        EmailNotificationItemRepository emailNotificationItemRepository,
        EmailNotificationItemMapper emailNotificationItemMapper
    ) {
        this.emailNotificationItemRepository = emailNotificationItemRepository;
        this.emailNotificationItemMapper = emailNotificationItemMapper;
    }

    /**
     * Save a emailNotificationItem.
     *
     * @param emailNotificationItemDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailNotificationItemDTO save(EmailNotificationItemDTO emailNotificationItemDTO) {
        LOG.debug("Request to save EmailNotificationItem : {}", emailNotificationItemDTO);
        EmailNotificationItem emailNotificationItem = emailNotificationItemMapper.toEntity(emailNotificationItemDTO);
        emailNotificationItem = emailNotificationItemRepository.save(emailNotificationItem);
        return emailNotificationItemMapper.toDto(emailNotificationItem);
    }

    /**
     * Update a emailNotificationItem.
     *
     * @param emailNotificationItemDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailNotificationItemDTO update(EmailNotificationItemDTO emailNotificationItemDTO) {
        LOG.debug("Request to update EmailNotificationItem : {}", emailNotificationItemDTO);
        EmailNotificationItem emailNotificationItem = emailNotificationItemMapper.toEntity(emailNotificationItemDTO);
        emailNotificationItem = emailNotificationItemRepository.save(emailNotificationItem);
        return emailNotificationItemMapper.toDto(emailNotificationItem);
    }

    /**
     * Partially update a emailNotificationItem.
     *
     * @param emailNotificationItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmailNotificationItemDTO> partialUpdate(EmailNotificationItemDTO emailNotificationItemDTO) {
        LOG.debug("Request to partially update EmailNotificationItem : {}", emailNotificationItemDTO);

        return emailNotificationItemRepository
            .findById(emailNotificationItemDTO.getId())
            .map(existingEmailNotificationItem -> {
                emailNotificationItemMapper.partialUpdate(existingEmailNotificationItem, emailNotificationItemDTO);

                return existingEmailNotificationItem;
            })
            .map(emailNotificationItemRepository::save)
            .map(emailNotificationItemMapper::toDto);
    }

    /**
     * Get all the emailNotificationItems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EmailNotificationItemDTO> findAll() {
        LOG.debug("Request to get all EmailNotificationItems");
        return emailNotificationItemRepository
            .findAll()
            .stream()
            .map(emailNotificationItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one emailNotificationItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmailNotificationItemDTO> findOne(Long id) {
        LOG.debug("Request to get EmailNotificationItem : {}", id);
        return emailNotificationItemRepository.findById(id).map(emailNotificationItemMapper::toDto);
    }

    /**
     * Delete the emailNotificationItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmailNotificationItem : {}", id);
        emailNotificationItemRepository.deleteById(id);
    }
}
