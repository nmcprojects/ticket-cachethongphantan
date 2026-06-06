package com.ticketbooking.payment.web.rest;

import com.ticketbooking.payment.repository.PaymentOutboxEventRepository;
import com.ticketbooking.payment.service.PaymentOutboxEventService;
import com.ticketbooking.payment.service.dto.PaymentOutboxEventDTO;
import com.ticketbooking.payment.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.ticketbooking.payment.domain.PaymentOutboxEvent}.
 */
@RestController
@RequestMapping("/api/payment-outbox-events")
public class PaymentOutboxEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentOutboxEventResource.class);

    private static final String ENTITY_NAME = "paymentServicePaymentOutboxEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentOutboxEventService paymentOutboxEventService;

    private final PaymentOutboxEventRepository paymentOutboxEventRepository;

    public PaymentOutboxEventResource(
        PaymentOutboxEventService paymentOutboxEventService,
        PaymentOutboxEventRepository paymentOutboxEventRepository
    ) {
        this.paymentOutboxEventService = paymentOutboxEventService;
        this.paymentOutboxEventRepository = paymentOutboxEventRepository;
    }

    /**
     * {@code POST  /payment-outbox-events} : Create a new paymentOutboxEvent.
     *
     * @param paymentOutboxEventDTO the paymentOutboxEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentOutboxEventDTO, or with status {@code 400 (Bad Request)} if the paymentOutboxEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentOutboxEventDTO> createPaymentOutboxEvent(@Valid @RequestBody PaymentOutboxEventDTO paymentOutboxEventDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PaymentOutboxEvent : {}", paymentOutboxEventDTO);
        if (paymentOutboxEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentOutboxEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentOutboxEventDTO = paymentOutboxEventService.save(paymentOutboxEventDTO);
        return ResponseEntity.created(new URI("/api/payment-outbox-events/" + paymentOutboxEventDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentOutboxEventDTO.getId().toString()))
            .body(paymentOutboxEventDTO);
    }

    /**
     * {@code PUT  /payment-outbox-events/:id} : Updates an existing paymentOutboxEvent.
     *
     * @param id the id of the paymentOutboxEventDTO to save.
     * @param paymentOutboxEventDTO the paymentOutboxEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentOutboxEventDTO,
     * or with status {@code 400 (Bad Request)} if the paymentOutboxEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentOutboxEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentOutboxEventDTO> updatePaymentOutboxEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentOutboxEventDTO paymentOutboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentOutboxEvent : {}, {}", id, paymentOutboxEventDTO);
        if (paymentOutboxEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentOutboxEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentOutboxEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentOutboxEventDTO = paymentOutboxEventService.update(paymentOutboxEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentOutboxEventDTO.getId().toString()))
            .body(paymentOutboxEventDTO);
    }

    /**
     * {@code PATCH  /payment-outbox-events/:id} : Partial updates given fields of an existing paymentOutboxEvent, field will ignore if it is null
     *
     * @param id the id of the paymentOutboxEventDTO to save.
     * @param paymentOutboxEventDTO the paymentOutboxEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentOutboxEventDTO,
     * or with status {@code 400 (Bad Request)} if the paymentOutboxEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentOutboxEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentOutboxEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentOutboxEventDTO> partialUpdatePaymentOutboxEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentOutboxEventDTO paymentOutboxEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentOutboxEvent partially : {}, {}", id, paymentOutboxEventDTO);
        if (paymentOutboxEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentOutboxEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentOutboxEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentOutboxEventDTO> result = paymentOutboxEventService.partialUpdate(paymentOutboxEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentOutboxEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-outbox-events} : get all the paymentOutboxEvents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentOutboxEvents in body.
     */
    @GetMapping("")
    public List<PaymentOutboxEventDTO> getAllPaymentOutboxEvents() {
        LOG.debug("REST request to get all PaymentOutboxEvents");
        return paymentOutboxEventService.findAll();
    }

    /**
     * {@code GET  /payment-outbox-events/:id} : get the "id" paymentOutboxEvent.
     *
     * @param id the id of the paymentOutboxEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentOutboxEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentOutboxEventDTO> getPaymentOutboxEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentOutboxEvent : {}", id);
        Optional<PaymentOutboxEventDTO> paymentOutboxEventDTO = paymentOutboxEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentOutboxEventDTO);
    }

    /**
     * {@code DELETE  /payment-outbox-events/:id} : delete the "id" paymentOutboxEvent.
     *
     * @param id the id of the paymentOutboxEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentOutboxEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentOutboxEvent : {}", id);
        paymentOutboxEventService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
