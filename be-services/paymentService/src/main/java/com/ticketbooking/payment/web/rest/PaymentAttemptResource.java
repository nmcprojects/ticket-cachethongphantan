package com.ticketbooking.payment.web.rest;

import com.ticketbooking.payment.repository.PaymentAttemptRepository;
import com.ticketbooking.payment.service.PaymentAttemptService;
import com.ticketbooking.payment.service.dto.PaymentAttemptDTO;
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
 * REST controller for managing {@link com.ticketbooking.payment.domain.PaymentAttempt}.
 */
@RestController
@RequestMapping("/api/payment-attempts")
public class PaymentAttemptResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentAttemptResource.class);

    private static final String ENTITY_NAME = "paymentServicePaymentAttempt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentAttemptService paymentAttemptService;

    private final PaymentAttemptRepository paymentAttemptRepository;

    public PaymentAttemptResource(PaymentAttemptService paymentAttemptService, PaymentAttemptRepository paymentAttemptRepository) {
        this.paymentAttemptService = paymentAttemptService;
        this.paymentAttemptRepository = paymentAttemptRepository;
    }

    /**
     * {@code POST  /payment-attempts} : Create a new paymentAttempt.
     *
     * @param paymentAttemptDTO the paymentAttemptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentAttemptDTO, or with status {@code 400 (Bad Request)} if the paymentAttempt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentAttemptDTO> createPaymentAttempt(@Valid @RequestBody PaymentAttemptDTO paymentAttemptDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PaymentAttempt : {}", paymentAttemptDTO);
        if (paymentAttemptDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentAttempt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentAttemptDTO = paymentAttemptService.save(paymentAttemptDTO);
        return ResponseEntity.created(new URI("/api/payment-attempts/" + paymentAttemptDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentAttemptDTO.getId().toString()))
            .body(paymentAttemptDTO);
    }

    /**
     * {@code PUT  /payment-attempts/:id} : Updates an existing paymentAttempt.
     *
     * @param id the id of the paymentAttemptDTO to save.
     * @param paymentAttemptDTO the paymentAttemptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentAttemptDTO,
     * or with status {@code 400 (Bad Request)} if the paymentAttemptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentAttemptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentAttemptDTO> updatePaymentAttempt(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentAttemptDTO paymentAttemptDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentAttempt : {}, {}", id, paymentAttemptDTO);
        if (paymentAttemptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentAttemptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentAttemptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentAttemptDTO = paymentAttemptService.update(paymentAttemptDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentAttemptDTO.getId().toString()))
            .body(paymentAttemptDTO);
    }

    /**
     * {@code PATCH  /payment-attempts/:id} : Partial updates given fields of an existing paymentAttempt, field will ignore if it is null
     *
     * @param id the id of the paymentAttemptDTO to save.
     * @param paymentAttemptDTO the paymentAttemptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentAttemptDTO,
     * or with status {@code 400 (Bad Request)} if the paymentAttemptDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentAttemptDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentAttemptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentAttemptDTO> partialUpdatePaymentAttempt(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentAttemptDTO paymentAttemptDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentAttempt partially : {}, {}", id, paymentAttemptDTO);
        if (paymentAttemptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentAttemptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentAttemptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentAttemptDTO> result = paymentAttemptService.partialUpdate(paymentAttemptDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentAttemptDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-attempts} : get all the paymentAttempts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentAttempts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentAttemptDTO>> getAllPaymentAttempts(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PaymentAttempts");
        Page<PaymentAttemptDTO> page = paymentAttemptService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-attempts/:id} : get the "id" paymentAttempt.
     *
     * @param id the id of the paymentAttemptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentAttemptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentAttemptDTO> getPaymentAttempt(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentAttempt : {}", id);
        Optional<PaymentAttemptDTO> paymentAttemptDTO = paymentAttemptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentAttemptDTO);
    }

    /**
     * {@code DELETE  /payment-attempts/:id} : delete the "id" paymentAttempt.
     *
     * @param id the id of the paymentAttemptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentAttempt(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentAttempt : {}", id);
        paymentAttemptService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
