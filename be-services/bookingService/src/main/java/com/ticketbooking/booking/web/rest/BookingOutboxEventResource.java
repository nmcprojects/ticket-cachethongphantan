package com.ticketbooking.booking.web.rest;

import com.ticketbooking.booking.repository.BookingOutboxEventRepository;
import com.ticketbooking.booking.service.BookingOutboxEventService;
import com.ticketbooking.booking.service.dto.BookingOutboxEventDTO;
import com.ticketbooking.booking.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.ticketbooking.booking.domain.BookingOutboxEvent}.
 */
@RestController
@RequestMapping("/api/booking-outbox-events")
public class BookingOutboxEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookingOutboxEventResource.class);

    private static final String ENTITY_NAME = "bookingServiceBookingOutboxEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookingOutboxEventService bookingOutboxEventService;

    private final BookingOutboxEventRepository bookingOutboxEventRepository;

    public BookingOutboxEventResource(
        BookingOutboxEventService bookingOutboxEventService,
        BookingOutboxEventRepository bookingOutboxEventRepository
    ) {
        this.bookingOutboxEventService = bookingOutboxEventService;
        this.bookingOutboxEventRepository = bookingOutboxEventRepository;
    }

    /**
     * {@code POST  /booking-outbox-events} : Create a new bookingOutboxEvent.
     *
     * @param bookingOutboxEventDTO the bookingOutboxEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookingOutboxEventDTO, or with status {@code 400 (Bad Request)} if the bookingOutboxEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookingOutboxEventDTO> createBookingOutboxEvent(@Valid @RequestBody BookingOutboxEventDTO bookingOutboxEventDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BookingOutboxEvent : {}", bookingOutboxEventDTO);
        if (bookingOutboxEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookingOutboxEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookingOutboxEventDTO = bookingOutboxEventService.save(bookingOutboxEventDTO);
        return ResponseEntity.created(new URI("/api/booking-outbox-events/" + bookingOutboxEventDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bookingOutboxEventDTO.getId().toString()))
            .body(bookingOutboxEventDTO);
    }

    /**
     * {@code PUT  /booking-outbox-events/:id} : Updates an existing bookingOutboxEvent.
     *
     * @param id the id of the bookingOutboxEventDTO to save.
     * @param bookingOutboxEventDTO the bookingOutboxEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingOutboxEventDTO,
     * or with status {@code 400 (Bad Request)} if the bookingOutboxEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookingOutboxEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingOutboxEventDTO> updateBookingOutboxEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookingOutboxEventDTO bookingOutboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookingOutboxEvent : {}, {}", id, bookingOutboxEventDTO);
        if (bookingOutboxEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingOutboxEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingOutboxEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookingOutboxEventDTO = bookingOutboxEventService.update(bookingOutboxEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookingOutboxEventDTO.getId().toString()))
            .body(bookingOutboxEventDTO);
    }

    /**
     * {@code PATCH  /booking-outbox-events/:id} : Partial updates given fields of an existing bookingOutboxEvent, field will ignore if it is null
     *
     * @param id the id of the bookingOutboxEventDTO to save.
     * @param bookingOutboxEventDTO the bookingOutboxEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingOutboxEventDTO,
     * or with status {@code 400 (Bad Request)} if the bookingOutboxEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookingOutboxEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookingOutboxEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookingOutboxEventDTO> partialUpdateBookingOutboxEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookingOutboxEventDTO bookingOutboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookingOutboxEvent partially : {}, {}", id, bookingOutboxEventDTO);
        if (bookingOutboxEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingOutboxEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingOutboxEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookingOutboxEventDTO> result = bookingOutboxEventService.partialUpdate(bookingOutboxEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookingOutboxEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /booking-outbox-events} : get all the bookingOutboxEvents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookingOutboxEvents in body.
     */
    @GetMapping("")
    public List<BookingOutboxEventDTO> getAllBookingOutboxEvents() {
        LOG.debug("REST request to get all BookingOutboxEvents");
        return bookingOutboxEventService.findAll();
    }

    /**
     * {@code GET  /booking-outbox-events/:id} : get the "id" bookingOutboxEvent.
     *
     * @param id the id of the bookingOutboxEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookingOutboxEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingOutboxEventDTO> getBookingOutboxEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookingOutboxEvent : {}", id);
        Optional<BookingOutboxEventDTO> bookingOutboxEventDTO = bookingOutboxEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingOutboxEventDTO);
    }

    /**
     * {@code DELETE  /booking-outbox-events/:id} : delete the "id" bookingOutboxEvent.
     *
     * @param id the id of the bookingOutboxEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingOutboxEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookingOutboxEvent : {}", id);
        bookingOutboxEventService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
