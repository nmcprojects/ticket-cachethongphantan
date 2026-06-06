package com.ticketbooking.ticket.web.rest;

import com.ticketbooking.ticket.repository.CheckinLogRepository;
import com.ticketbooking.ticket.service.CheckinLogService;
import com.ticketbooking.ticket.service.dto.CheckinLogDTO;
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
 * REST controller for managing {@link com.ticketbooking.ticket.domain.CheckinLog}.
 */
@RestController
@RequestMapping("/api/checkin-logs")
public class CheckinLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(CheckinLogResource.class);

    private static final String ENTITY_NAME = "ticketServiceCheckinLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CheckinLogService checkinLogService;

    private final CheckinLogRepository checkinLogRepository;

    public CheckinLogResource(CheckinLogService checkinLogService, CheckinLogRepository checkinLogRepository) {
        this.checkinLogService = checkinLogService;
        this.checkinLogRepository = checkinLogRepository;
    }

    /**
     * {@code POST  /checkin-logs} : Create a new checkinLog.
     *
     * @param checkinLogDTO the checkinLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new checkinLogDTO, or with status {@code 400 (Bad Request)} if the checkinLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CheckinLogDTO> createCheckinLog(@Valid @RequestBody CheckinLogDTO checkinLogDTO) throws URISyntaxException {
        LOG.debug("REST request to save CheckinLog : {}", checkinLogDTO);
        if (checkinLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new checkinLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        checkinLogDTO = checkinLogService.save(checkinLogDTO);
        return ResponseEntity.created(new URI("/api/checkin-logs/" + checkinLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, checkinLogDTO.getId().toString()))
            .body(checkinLogDTO);
    }

    /**
     * {@code PUT  /checkin-logs/:id} : Updates an existing checkinLog.
     *
     * @param id the id of the checkinLogDTO to save.
     * @param checkinLogDTO the checkinLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated checkinLogDTO,
     * or with status {@code 400 (Bad Request)} if the checkinLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the checkinLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CheckinLogDTO> updateCheckinLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CheckinLogDTO checkinLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CheckinLog : {}, {}", id, checkinLogDTO);
        if (checkinLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, checkinLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!checkinLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        checkinLogDTO = checkinLogService.update(checkinLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, checkinLogDTO.getId().toString()))
            .body(checkinLogDTO);
    }

    /**
     * {@code PATCH  /checkin-logs/:id} : Partial updates given fields of an existing checkinLog, field will ignore if it is null
     *
     * @param id the id of the checkinLogDTO to save.
     * @param checkinLogDTO the checkinLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated checkinLogDTO,
     * or with status {@code 400 (Bad Request)} if the checkinLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the checkinLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the checkinLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CheckinLogDTO> partialUpdateCheckinLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CheckinLogDTO checkinLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CheckinLog partially : {}, {}", id, checkinLogDTO);
        if (checkinLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, checkinLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!checkinLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CheckinLogDTO> result = checkinLogService.partialUpdate(checkinLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, checkinLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /checkin-logs} : get all the checkinLogs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of checkinLogs in body.
     */
    @GetMapping("")
    public List<CheckinLogDTO> getAllCheckinLogs() {
        LOG.debug("REST request to get all CheckinLogs");
        return checkinLogService.findAll();
    }

    /**
     * {@code GET  /checkin-logs/:id} : get the "id" checkinLog.
     *
     * @param id the id of the checkinLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the checkinLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CheckinLogDTO> getCheckinLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CheckinLog : {}", id);
        Optional<CheckinLogDTO> checkinLogDTO = checkinLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(checkinLogDTO);
    }

    /**
     * {@code DELETE  /checkin-logs/:id} : delete the "id" checkinLog.
     *
     * @param id the id of the checkinLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheckinLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CheckinLog : {}", id);
        checkinLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
