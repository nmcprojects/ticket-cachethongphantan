package com.ticketbooking.notification.web.rest;

import com.ticketbooking.notification.repository.EmailNotificationItemRepository;
import com.ticketbooking.notification.service.EmailNotificationItemService;
import com.ticketbooking.notification.service.dto.EmailNotificationItemDTO;
import com.ticketbooking.notification.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ticketbooking.notification.domain.EmailNotificationItem}.
 */
@RestController
@RequestMapping("/api/email-notification-items")
public class EmailNotificationItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmailNotificationItemResource.class);

    private static final String ENTITY_NAME = "notificationServiceEmailNotificationItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailNotificationItemService emailNotificationItemService;

    private final EmailNotificationItemRepository emailNotificationItemRepository;

    public EmailNotificationItemResource(
        EmailNotificationItemService emailNotificationItemService,
        EmailNotificationItemRepository emailNotificationItemRepository
    ) {
        this.emailNotificationItemService = emailNotificationItemService;
        this.emailNotificationItemRepository = emailNotificationItemRepository;
    }

    /**
     * {@code POST  /email-notification-items} : Create a new emailNotificationItem.
     *
     * @param emailNotificationItemDTO the emailNotificationItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailNotificationItemDTO, or with status {@code 400 (Bad Request)} if the emailNotificationItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmailNotificationItemDTO> createEmailNotificationItem(
        @Valid @RequestBody EmailNotificationItemDTO emailNotificationItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save EmailNotificationItem : {}", emailNotificationItemDTO);
        if (emailNotificationItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new emailNotificationItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        emailNotificationItemDTO = emailNotificationItemService.save(emailNotificationItemDTO);
        return ResponseEntity.created(new URI("/api/email-notification-items/" + emailNotificationItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, emailNotificationItemDTO.getId().toString()))
            .body(emailNotificationItemDTO);
    }

    /**
     * {@code PUT  /email-notification-items/:id} : Updates an existing emailNotificationItem.
     *
     * @param id the id of the emailNotificationItemDTO to save.
     * @param emailNotificationItemDTO the emailNotificationItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailNotificationItemDTO,
     * or with status {@code 400 (Bad Request)} if the emailNotificationItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailNotificationItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmailNotificationItemDTO> updateEmailNotificationItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmailNotificationItemDTO emailNotificationItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmailNotificationItem : {}, {}", id, emailNotificationItemDTO);
        if (emailNotificationItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailNotificationItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailNotificationItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        emailNotificationItemDTO = emailNotificationItemService.update(emailNotificationItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailNotificationItemDTO.getId().toString()))
            .body(emailNotificationItemDTO);
    }

    /**
     * {@code PATCH  /email-notification-items/:id} : Partial updates given fields of an existing emailNotificationItem, field will ignore if it is null
     *
     * @param id the id of the emailNotificationItemDTO to save.
     * @param emailNotificationItemDTO the emailNotificationItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailNotificationItemDTO,
     * or with status {@code 400 (Bad Request)} if the emailNotificationItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emailNotificationItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailNotificationItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmailNotificationItemDTO> partialUpdateEmailNotificationItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmailNotificationItemDTO emailNotificationItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmailNotificationItem partially : {}, {}", id, emailNotificationItemDTO);
        if (emailNotificationItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailNotificationItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailNotificationItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailNotificationItemDTO> result = emailNotificationItemService.partialUpdate(emailNotificationItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailNotificationItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /email-notification-items} : get all the emailNotificationItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailNotificationItems in body.
     */
    @GetMapping("")
    public List<EmailNotificationItemDTO> getAllEmailNotificationItems() {
        LOG.debug("REST request to get all EmailNotificationItems");
        return emailNotificationItemService.findAll();
    }

    /**
     * {@code GET  /email-notification-items/:id} : get the "id" emailNotificationItem.
     *
     * @param id the id of the emailNotificationItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailNotificationItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmailNotificationItemDTO> getEmailNotificationItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmailNotificationItem : {}", id);
        Optional<EmailNotificationItemDTO> emailNotificationItemDTO = emailNotificationItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailNotificationItemDTO);
    }

    /**
     * {@code DELETE  /email-notification-items/:id} : delete the "id" emailNotificationItem.
     *
     * @param id the id of the emailNotificationItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailNotificationItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmailNotificationItem : {}", id);
        emailNotificationItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
