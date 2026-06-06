package com.ticketbooking.notification.web.rest;

import com.ticketbooking.notification.repository.NotificationInboxEventRepository;
import com.ticketbooking.notification.service.NotificationInboxEventService;
import com.ticketbooking.notification.service.dto.NotificationInboxEventDTO;
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
 * REST controller for managing {@link com.ticketbooking.notification.domain.NotificationInboxEvent}.
 */
@RestController
@RequestMapping("/api/notification-inbox-events")
public class NotificationInboxEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationInboxEventResource.class);

    private static final String ENTITY_NAME = "notificationServiceNotificationInboxEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationInboxEventService notificationInboxEventService;

    private final NotificationInboxEventRepository notificationInboxEventRepository;

    public NotificationInboxEventResource(
        NotificationInboxEventService notificationInboxEventService,
        NotificationInboxEventRepository notificationInboxEventRepository
    ) {
        this.notificationInboxEventService = notificationInboxEventService;
        this.notificationInboxEventRepository = notificationInboxEventRepository;
    }

    /**
     * {@code POST  /notification-inbox-events} : Create a new notificationInboxEvent.
     *
     * @param notificationInboxEventDTO the notificationInboxEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationInboxEventDTO, or with status {@code 400 (Bad Request)} if the notificationInboxEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NotificationInboxEventDTO> createNotificationInboxEvent(
        @Valid @RequestBody NotificationInboxEventDTO notificationInboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save NotificationInboxEvent : {}", notificationInboxEventDTO);
        if (notificationInboxEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificationInboxEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        notificationInboxEventDTO = notificationInboxEventService.save(notificationInboxEventDTO);
        return ResponseEntity.created(new URI("/api/notification-inbox-events/" + notificationInboxEventDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, notificationInboxEventDTO.getId().toString()))
            .body(notificationInboxEventDTO);
    }

    /**
     * {@code PUT  /notification-inbox-events/:id} : Updates an existing notificationInboxEvent.
     *
     * @param id the id of the notificationInboxEventDTO to save.
     * @param notificationInboxEventDTO the notificationInboxEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationInboxEventDTO,
     * or with status {@code 400 (Bad Request)} if the notificationInboxEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationInboxEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotificationInboxEventDTO> updateNotificationInboxEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NotificationInboxEventDTO notificationInboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NotificationInboxEvent : {}, {}", id, notificationInboxEventDTO);
        if (notificationInboxEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationInboxEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationInboxEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        notificationInboxEventDTO = notificationInboxEventService.update(notificationInboxEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationInboxEventDTO.getId().toString()))
            .body(notificationInboxEventDTO);
    }

    /**
     * {@code PATCH  /notification-inbox-events/:id} : Partial updates given fields of an existing notificationInboxEvent, field will ignore if it is null
     *
     * @param id the id of the notificationInboxEventDTO to save.
     * @param notificationInboxEventDTO the notificationInboxEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationInboxEventDTO,
     * or with status {@code 400 (Bad Request)} if the notificationInboxEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notificationInboxEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificationInboxEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotificationInboxEventDTO> partialUpdateNotificationInboxEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NotificationInboxEventDTO notificationInboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NotificationInboxEvent partially : {}, {}", id, notificationInboxEventDTO);
        if (notificationInboxEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationInboxEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationInboxEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificationInboxEventDTO> result = notificationInboxEventService.partialUpdate(notificationInboxEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationInboxEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /notification-inbox-events} : get all the notificationInboxEvents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationInboxEvents in body.
     */
    @GetMapping("")
    public List<NotificationInboxEventDTO> getAllNotificationInboxEvents() {
        LOG.debug("REST request to get all NotificationInboxEvents");
        return notificationInboxEventService.findAll();
    }

    /**
     * {@code GET  /notification-inbox-events/:id} : get the "id" notificationInboxEvent.
     *
     * @param id the id of the notificationInboxEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationInboxEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationInboxEventDTO> getNotificationInboxEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NotificationInboxEvent : {}", id);
        Optional<NotificationInboxEventDTO> notificationInboxEventDTO = notificationInboxEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationInboxEventDTO);
    }

    /**
     * {@code DELETE  /notification-inbox-events/:id} : delete the "id" notificationInboxEvent.
     *
     * @param id the id of the notificationInboxEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificationInboxEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NotificationInboxEvent : {}", id);
        notificationInboxEventService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
