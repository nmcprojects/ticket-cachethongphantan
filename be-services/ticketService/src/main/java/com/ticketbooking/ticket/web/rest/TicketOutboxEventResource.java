package com.ticketbooking.ticket.web.rest;

import com.ticketbooking.ticket.repository.TicketOutboxEventRepository;
import com.ticketbooking.ticket.service.TicketOutboxEventService;
import com.ticketbooking.ticket.service.dto.TicketOutboxEventDTO;
import com.ticketbooking.ticket.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.ticketbooking.ticket.domain.TicketOutboxEvent}.
 */
@RestController
@RequestMapping("/api/ticket-outbox-events")
public class TicketOutboxEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(TicketOutboxEventResource.class);

    private static final String ENTITY_NAME = "ticketServiceTicketOutboxEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketOutboxEventService ticketOutboxEventService;

    private final TicketOutboxEventRepository ticketOutboxEventRepository;

    public TicketOutboxEventResource(
        TicketOutboxEventService ticketOutboxEventService,
        TicketOutboxEventRepository ticketOutboxEventRepository
    ) {
        this.ticketOutboxEventService = ticketOutboxEventService;
        this.ticketOutboxEventRepository = ticketOutboxEventRepository;
    }

    /**
     * {@code POST  /ticket-outbox-events} : Create a new ticketOutboxEvent.
     *
     * @param ticketOutboxEventDTO the ticketOutboxEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketOutboxEventDTO, or with status {@code 400 (Bad Request)} if the ticketOutboxEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TicketOutboxEventDTO> createTicketOutboxEvent(@Valid @RequestBody TicketOutboxEventDTO ticketOutboxEventDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TicketOutboxEvent : {}", ticketOutboxEventDTO);
        if (ticketOutboxEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new ticketOutboxEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ticketOutboxEventDTO = ticketOutboxEventService.save(ticketOutboxEventDTO);
        return ResponseEntity.created(new URI("/api/ticket-outbox-events/" + ticketOutboxEventDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ticketOutboxEventDTO.getId().toString()))
            .body(ticketOutboxEventDTO);
    }

    /**
     * {@code PUT  /ticket-outbox-events/:id} : Updates an existing ticketOutboxEvent.
     *
     * @param id the id of the ticketOutboxEventDTO to save.
     * @param ticketOutboxEventDTO the ticketOutboxEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketOutboxEventDTO,
     * or with status {@code 400 (Bad Request)} if the ticketOutboxEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticketOutboxEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TicketOutboxEventDTO> updateTicketOutboxEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TicketOutboxEventDTO ticketOutboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TicketOutboxEvent : {}, {}", id, ticketOutboxEventDTO);
        if (ticketOutboxEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketOutboxEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketOutboxEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ticketOutboxEventDTO = ticketOutboxEventService.update(ticketOutboxEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticketOutboxEventDTO.getId().toString()))
            .body(ticketOutboxEventDTO);
    }

    /**
     * {@code PATCH  /ticket-outbox-events/:id} : Partial updates given fields of an existing ticketOutboxEvent, field will ignore if it is null
     *
     * @param id the id of the ticketOutboxEventDTO to save.
     * @param ticketOutboxEventDTO the ticketOutboxEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketOutboxEventDTO,
     * or with status {@code 400 (Bad Request)} if the ticketOutboxEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ticketOutboxEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ticketOutboxEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TicketOutboxEventDTO> partialUpdateTicketOutboxEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TicketOutboxEventDTO ticketOutboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TicketOutboxEvent partially : {}, {}", id, ticketOutboxEventDTO);
        if (ticketOutboxEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketOutboxEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketOutboxEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TicketOutboxEventDTO> result = ticketOutboxEventService.partialUpdate(ticketOutboxEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticketOutboxEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ticket-outbox-events} : get all the ticketOutboxEvents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ticketOutboxEvents in body.
     */
    @GetMapping("")
    public List<TicketOutboxEventDTO> getAllTicketOutboxEvents() {
        LOG.debug("REST request to get all TicketOutboxEvents");
        return ticketOutboxEventService.findAll();
    }

    /**
     * {@code GET  /ticket-outbox-events/:id} : get the "id" ticketOutboxEvent.
     *
     * @param id the id of the ticketOutboxEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketOutboxEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketOutboxEventDTO> getTicketOutboxEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TicketOutboxEvent : {}", id);
        Optional<TicketOutboxEventDTO> ticketOutboxEventDTO = ticketOutboxEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ticketOutboxEventDTO);
    }

    /**
     * {@code DELETE  /ticket-outbox-events/:id} : delete the "id" ticketOutboxEvent.
     *
     * @param id the id of the ticketOutboxEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketOutboxEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TicketOutboxEvent : {}", id);
        ticketOutboxEventService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
