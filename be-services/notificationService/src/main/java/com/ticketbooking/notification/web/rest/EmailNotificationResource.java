package com.ticketbooking.notification.web.rest;

import com.ticketbooking.notification.repository.EmailNotificationRepository;
import com.ticketbooking.notification.service.EmailNotificationService;
import com.ticketbooking.notification.service.dto.EmailNotificationDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ticketbooking.notification.domain.EmailNotification}.
 */
@RestController
@RequestMapping("/api/email-notifications")
public class EmailNotificationResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmailNotificationResource.class);

    private static final String ENTITY_NAME = "notificationServiceEmailNotification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailNotificationService emailNotificationService;

    private final EmailNotificationRepository emailNotificationRepository;

    public EmailNotificationResource(
        EmailNotificationService emailNotificationService,
        EmailNotificationRepository emailNotificationRepository
    ) {
        this.emailNotificationService = emailNotificationService;
        this.emailNotificationRepository = emailNotificationRepository;
    }

    /**
     * {@code POST  /email-notifications} : Create a new emailNotification.
     *
     * @param emailNotificationDTO the emailNotificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailNotificationDTO, or with status {@code 400 (Bad Request)} if the emailNotification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmailNotificationDTO> createEmailNotification(@Valid @RequestBody EmailNotificationDTO emailNotificationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EmailNotification : {}", emailNotificationDTO);
        if (emailNotificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new emailNotification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        emailNotificationDTO = emailNotificationService.save(emailNotificationDTO);
        return ResponseEntity.created(new URI("/api/email-notifications/" + emailNotificationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, emailNotificationDTO.getId().toString()))
            .body(emailNotificationDTO);
    }

    /**
     * {@code PUT  /email-notifications/:id} : Updates an existing emailNotification.
     *
     * @param id the id of the emailNotificationDTO to save.
     * @param emailNotificationDTO the emailNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the emailNotificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmailNotificationDTO> updateEmailNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmailNotificationDTO emailNotificationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmailNotification : {}, {}", id, emailNotificationDTO);
        if (emailNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        emailNotificationDTO = emailNotificationService.update(emailNotificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailNotificationDTO.getId().toString()))
            .body(emailNotificationDTO);
    }

    /**
     * {@code PATCH  /email-notifications/:id} : Partial updates given fields of an existing emailNotification, field will ignore if it is null
     *
     * @param id the id of the emailNotificationDTO to save.
     * @param emailNotificationDTO the emailNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the emailNotificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emailNotificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmailNotificationDTO> partialUpdateEmailNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmailNotificationDTO emailNotificationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmailNotification partially : {}, {}", id, emailNotificationDTO);
        if (emailNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailNotificationDTO> result = emailNotificationService.partialUpdate(emailNotificationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailNotificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /email-notifications} : get all the emailNotifications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailNotifications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmailNotificationDTO>> getAllEmailNotifications(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of EmailNotifications");
        Page<EmailNotificationDTO> page = emailNotificationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /email-notifications/:id} : get the "id" emailNotification.
     *
     * @param id the id of the emailNotificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailNotificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmailNotificationDTO> getEmailNotification(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmailNotification : {}", id);
        Optional<EmailNotificationDTO> emailNotificationDTO = emailNotificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailNotificationDTO);
    }

    /**
     * {@code DELETE  /email-notifications/:id} : delete the "id" emailNotification.
     *
     * @param id the id of the emailNotificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailNotification(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmailNotification : {}", id);
        emailNotificationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
