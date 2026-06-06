package com.ticketbooking.booking.web.rest;

import com.ticketbooking.booking.repository.BookingInboxEventRepository;
import com.ticketbooking.booking.service.BookingInboxEventService;
import com.ticketbooking.booking.service.dto.BookingInboxEventDTO;
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
 * REST controller for managing {@link com.ticketbooking.booking.domain.BookingInboxEvent}.
 */
@RestController
@RequestMapping("/api/booking-inbox-events")
public class BookingInboxEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookingInboxEventResource.class);

    private static final String ENTITY_NAME = "bookingServiceBookingInboxEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookingInboxEventService bookingInboxEventService;

    private final BookingInboxEventRepository bookingInboxEventRepository;

    public BookingInboxEventResource(
        BookingInboxEventService bookingInboxEventService,
        BookingInboxEventRepository bookingInboxEventRepository
    ) {
        this.bookingInboxEventService = bookingInboxEventService;
        this.bookingInboxEventRepository = bookingInboxEventRepository;
    }

    /**
     * {@code POST  /booking-inbox-events} : Create a new bookingInboxEvent.
     *
     * @param bookingInboxEventDTO the bookingInboxEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookingInboxEventDTO, or with status {@code 400 (Bad Request)} if the bookingInboxEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookingInboxEventDTO> createBookingInboxEvent(@Valid @RequestBody BookingInboxEventDTO bookingInboxEventDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BookingInboxEvent : {}", bookingInboxEventDTO);
        if (bookingInboxEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookingInboxEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookingInboxEventDTO = bookingInboxEventService.save(bookingInboxEventDTO);
        return ResponseEntity.created(new URI("/api/booking-inbox-events/" + bookingInboxEventDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bookingInboxEventDTO.getId().toString()))
            .body(bookingInboxEventDTO);
    }

    /**
     * {@code PUT  /booking-inbox-events/:id} : Updates an existing bookingInboxEvent.
     *
     * @param id the id of the bookingInboxEventDTO to save.
     * @param bookingInboxEventDTO the bookingInboxEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingInboxEventDTO,
     * or with status {@code 400 (Bad Request)} if the bookingInboxEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookingInboxEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingInboxEventDTO> updateBookingInboxEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookingInboxEventDTO bookingInboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BookingInboxEvent : {}, {}", id, bookingInboxEventDTO);
        if (bookingInboxEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingInboxEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingInboxEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookingInboxEventDTO = bookingInboxEventService.update(bookingInboxEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookingInboxEventDTO.getId().toString()))
            .body(bookingInboxEventDTO);
    }

    /**
     * {@code PATCH  /booking-inbox-events/:id} : Partial updates given fields of an existing bookingInboxEvent, field will ignore if it is null
     *
     * @param id the id of the bookingInboxEventDTO to save.
     * @param bookingInboxEventDTO the bookingInboxEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingInboxEventDTO,
     * or with status {@code 400 (Bad Request)} if the bookingInboxEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookingInboxEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookingInboxEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookingInboxEventDTO> partialUpdateBookingInboxEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookingInboxEventDTO bookingInboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BookingInboxEvent partially : {}, {}", id, bookingInboxEventDTO);
        if (bookingInboxEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingInboxEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingInboxEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookingInboxEventDTO> result = bookingInboxEventService.partialUpdate(bookingInboxEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookingInboxEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /booking-inbox-events} : get all the bookingInboxEvents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookingInboxEvents in body.
     */
    @GetMapping("")
    public List<BookingInboxEventDTO> getAllBookingInboxEvents() {
        LOG.debug("REST request to get all BookingInboxEvents");
        return bookingInboxEventService.findAll();
    }

    /**
     * {@code GET  /booking-inbox-events/:id} : get the "id" bookingInboxEvent.
     *
     * @param id the id of the bookingInboxEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookingInboxEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingInboxEventDTO> getBookingInboxEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BookingInboxEvent : {}", id);
        Optional<BookingInboxEventDTO> bookingInboxEventDTO = bookingInboxEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingInboxEventDTO);
    }

    /**
     * {@code DELETE  /booking-inbox-events/:id} : delete the "id" bookingInboxEvent.
     *
     * @param id the id of the bookingInboxEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingInboxEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BookingInboxEvent : {}", id);
        bookingInboxEventService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
