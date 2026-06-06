package com.ticketbooking.event.web.rest;

import com.ticketbooking.event.repository.TicketReservationRepository;
import com.ticketbooking.event.service.TicketReservationService;
import com.ticketbooking.event.service.dto.TicketReservationDTO;
import com.ticketbooking.event.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.ticketbooking.event.domain.TicketReservation}.
 */
@RestController
@RequestMapping("/api/ticket-reservations")
public class TicketReservationResource {

    private static final Logger LOG = LoggerFactory.getLogger(TicketReservationResource.class);

    private static final String ENTITY_NAME = "eventServiceTicketReservation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketReservationService ticketReservationService;

    private final TicketReservationRepository ticketReservationRepository;

    public TicketReservationResource(
        TicketReservationService ticketReservationService,
        TicketReservationRepository ticketReservationRepository
    ) {
        this.ticketReservationService = ticketReservationService;
        this.ticketReservationRepository = ticketReservationRepository;
    }

    /**
     * {@code POST  /ticket-reservations} : Create a new ticketReservation.
     *
     * @param ticketReservationDTO the ticketReservationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketReservationDTO, or with status {@code 400 (Bad Request)} if the ticketReservation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TicketReservationDTO> createTicketReservation(@Valid @RequestBody TicketReservationDTO ticketReservationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TicketReservation : {}", ticketReservationDTO);
        if (ticketReservationDTO.getId() != null) {
            throw new BadRequestAlertException("A new ticketReservation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ticketReservationDTO = ticketReservationService.save(ticketReservationDTO);
        return ResponseEntity.created(new URI("/api/ticket-reservations/" + ticketReservationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ticketReservationDTO.getId().toString()))
            .body(ticketReservationDTO);
    }

    /**
     * {@code PUT  /ticket-reservations/:id} : Updates an existing ticketReservation.
     *
     * @param id the id of the ticketReservationDTO to save.
     * @param ticketReservationDTO the ticketReservationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketReservationDTO,
     * or with status {@code 400 (Bad Request)} if the ticketReservationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticketReservationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TicketReservationDTO> updateTicketReservation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TicketReservationDTO ticketReservationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TicketReservation : {}, {}", id, ticketReservationDTO);
        if (ticketReservationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketReservationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketReservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ticketReservationDTO = ticketReservationService.update(ticketReservationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticketReservationDTO.getId().toString()))
            .body(ticketReservationDTO);
    }

    /**
     * {@code PATCH  /ticket-reservations/:id} : Partial updates given fields of an existing ticketReservation, field will ignore if it is null
     *
     * @param id the id of the ticketReservationDTO to save.
     * @param ticketReservationDTO the ticketReservationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketReservationDTO,
     * or with status {@code 400 (Bad Request)} if the ticketReservationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ticketReservationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ticketReservationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TicketReservationDTO> partialUpdateTicketReservation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TicketReservationDTO ticketReservationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TicketReservation partially : {}, {}", id, ticketReservationDTO);
        if (ticketReservationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketReservationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketReservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TicketReservationDTO> result = ticketReservationService.partialUpdate(ticketReservationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticketReservationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ticket-reservations} : get all the ticketReservations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ticketReservations in body.
     */
    @GetMapping("")
    public List<TicketReservationDTO> getAllTicketReservations() {
        LOG.debug("REST request to get all TicketReservations");
        return ticketReservationService.findAll();
    }

    /**
     * {@code GET  /ticket-reservations/:id} : get the "id" ticketReservation.
     *
     * @param id the id of the ticketReservationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketReservationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketReservationDTO> getTicketReservation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TicketReservation : {}", id);
        Optional<TicketReservationDTO> ticketReservationDTO = ticketReservationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ticketReservationDTO);
    }

    /**
     * {@code DELETE  /ticket-reservations/:id} : delete the "id" ticketReservation.
     *
     * @param id the id of the ticketReservationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketReservation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TicketReservation : {}", id);
        ticketReservationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
