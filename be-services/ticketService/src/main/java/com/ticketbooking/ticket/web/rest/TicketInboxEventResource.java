package com.ticketbooking.ticket.web.rest;

import com.ticketbooking.ticket.repository.TicketInboxEventRepository;
import com.ticketbooking.ticket.service.TicketInboxEventService;
import com.ticketbooking.ticket.service.dto.TicketInboxEventDTO;
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
 * REST controller for managing {@link com.ticketbooking.ticket.domain.TicketInboxEvent}.
 */
@RestController
@RequestMapping("/api/ticket-inbox-events")
public class TicketInboxEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(TicketInboxEventResource.class);

    private static final String ENTITY_NAME = "ticketServiceTicketInboxEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketInboxEventService ticketInboxEventService;

    private final TicketInboxEventRepository ticketInboxEventRepository;

    public TicketInboxEventResource(
        TicketInboxEventService ticketInboxEventService,
        TicketInboxEventRepository ticketInboxEventRepository
    ) {
        this.ticketInboxEventService = ticketInboxEventService;
        this.ticketInboxEventRepository = ticketInboxEventRepository;
    }

    /**
     * {@code POST  /ticket-inbox-events} : Create a new ticketInboxEvent.
     *
     * @param ticketInboxEventDTO the ticketInboxEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketInboxEventDTO, or with status {@code 400 (Bad Request)} if the ticketInboxEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TicketInboxEventDTO> createTicketInboxEvent(@Valid @RequestBody TicketInboxEventDTO ticketInboxEventDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TicketInboxEvent : {}", ticketInboxEventDTO);
        if (ticketInboxEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new ticketInboxEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ticketInboxEventDTO = ticketInboxEventService.save(ticketInboxEventDTO);
        return ResponseEntity.created(new URI("/api/ticket-inbox-events/" + ticketInboxEventDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ticketInboxEventDTO.getId().toString()))
            .body(ticketInboxEventDTO);
    }

    /**
     * {@code PUT  /ticket-inbox-events/:id} : Updates an existing ticketInboxEvent.
     *
     * @param id the id of the ticketInboxEventDTO to save.
     * @param ticketInboxEventDTO the ticketInboxEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketInboxEventDTO,
     * or with status {@code 400 (Bad Request)} if the ticketInboxEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticketInboxEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TicketInboxEventDTO> updateTicketInboxEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TicketInboxEventDTO ticketInboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TicketInboxEvent : {}, {}", id, ticketInboxEventDTO);
        if (ticketInboxEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketInboxEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketInboxEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ticketInboxEventDTO = ticketInboxEventService.update(ticketInboxEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticketInboxEventDTO.getId().toString()))
            .body(ticketInboxEventDTO);
    }

    /**
     * {@code PATCH  /ticket-inbox-events/:id} : Partial updates given fields of an existing ticketInboxEvent, field will ignore if it is null
     *
     * @param id the id of the ticketInboxEventDTO to save.
     * @param ticketInboxEventDTO the ticketInboxEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketInboxEventDTO,
     * or with status {@code 400 (Bad Request)} if the ticketInboxEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ticketInboxEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ticketInboxEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TicketInboxEventDTO> partialUpdateTicketInboxEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TicketInboxEventDTO ticketInboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TicketInboxEvent partially : {}, {}", id, ticketInboxEventDTO);
        if (ticketInboxEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketInboxEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketInboxEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TicketInboxEventDTO> result = ticketInboxEventService.partialUpdate(ticketInboxEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticketInboxEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ticket-inbox-events} : get all the ticketInboxEvents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ticketInboxEvents in body.
     */
    @GetMapping("")
    public List<TicketInboxEventDTO> getAllTicketInboxEvents() {
        LOG.debug("REST request to get all TicketInboxEvents");
        return ticketInboxEventService.findAll();
    }

    /**
     * {@code GET  /ticket-inbox-events/:id} : get the "id" ticketInboxEvent.
     *
     * @param id the id of the ticketInboxEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketInboxEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketInboxEventDTO> getTicketInboxEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TicketInboxEvent : {}", id);
        Optional<TicketInboxEventDTO> ticketInboxEventDTO = ticketInboxEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ticketInboxEventDTO);
    }

    /**
     * {@code DELETE  /ticket-inbox-events/:id} : delete the "id" ticketInboxEvent.
     *
     * @param id the id of the ticketInboxEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketInboxEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TicketInboxEvent : {}", id);
        ticketInboxEventService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
