package com.ticketbooking.event.web.rest;

import com.ticketbooking.event.repository.OrganizerProfileRepository;
import com.ticketbooking.event.service.OrganizerProfileService;
import com.ticketbooking.event.service.dto.OrganizerProfileDTO;
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
 * REST controller for managing {@link com.ticketbooking.event.domain.OrganizerProfile}.
 */
@RestController
@RequestMapping("/api/organizer-profiles")
public class OrganizerProfileResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizerProfileResource.class);

    private static final String ENTITY_NAME = "eventServiceOrganizerProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizerProfileService organizerProfileService;

    private final OrganizerProfileRepository organizerProfileRepository;

    public OrganizerProfileResource(
        OrganizerProfileService organizerProfileService,
        OrganizerProfileRepository organizerProfileRepository
    ) {
        this.organizerProfileService = organizerProfileService;
        this.organizerProfileRepository = organizerProfileRepository;
    }

    /**
     * {@code POST  /organizer-profiles} : Create a new organizerProfile.
     *
     * @param organizerProfileDTO the organizerProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizerProfileDTO, or with status {@code 400 (Bad Request)} if the organizerProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrganizerProfileDTO> createOrganizerProfile(@Valid @RequestBody OrganizerProfileDTO organizerProfileDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save OrganizerProfile : {}", organizerProfileDTO);
        if (organizerProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new organizerProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        organizerProfileDTO = organizerProfileService.save(organizerProfileDTO);
        return ResponseEntity.created(new URI("/api/organizer-profiles/" + organizerProfileDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, organizerProfileDTO.getId().toString()))
            .body(organizerProfileDTO);
    }

    /**
     * {@code PUT  /organizer-profiles/:id} : Updates an existing organizerProfile.
     *
     * @param id the id of the organizerProfileDTO to save.
     * @param organizerProfileDTO the organizerProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizerProfileDTO,
     * or with status {@code 400 (Bad Request)} if the organizerProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizerProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrganizerProfileDTO> updateOrganizerProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrganizerProfileDTO organizerProfileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrganizerProfile : {}, {}", id, organizerProfileDTO);
        if (organizerProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizerProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizerProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        organizerProfileDTO = organizerProfileService.update(organizerProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizerProfileDTO.getId().toString()))
            .body(organizerProfileDTO);
    }

    /**
     * {@code PATCH  /organizer-profiles/:id} : Partial updates given fields of an existing organizerProfile, field will ignore if it is null
     *
     * @param id the id of the organizerProfileDTO to save.
     * @param organizerProfileDTO the organizerProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizerProfileDTO,
     * or with status {@code 400 (Bad Request)} if the organizerProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the organizerProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the organizerProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrganizerProfileDTO> partialUpdateOrganizerProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrganizerProfileDTO organizerProfileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrganizerProfile partially : {}, {}", id, organizerProfileDTO);
        if (organizerProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizerProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizerProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrganizerProfileDTO> result = organizerProfileService.partialUpdate(organizerProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizerProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /organizer-profiles} : get all the organizerProfiles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizerProfiles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrganizerProfileDTO>> getAllOrganizerProfiles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of OrganizerProfiles");
        Page<OrganizerProfileDTO> page = organizerProfileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organizer-profiles/:id} : get the "id" organizerProfile.
     *
     * @param id the id of the organizerProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizerProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrganizerProfileDTO> getOrganizerProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrganizerProfile : {}", id);
        Optional<OrganizerProfileDTO> organizerProfileDTO = organizerProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizerProfileDTO);
    }

    /**
     * {@code DELETE  /organizer-profiles/:id} : delete the "id" organizerProfile.
     *
     * @param id the id of the organizerProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganizerProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrganizerProfile : {}", id);
        organizerProfileService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
