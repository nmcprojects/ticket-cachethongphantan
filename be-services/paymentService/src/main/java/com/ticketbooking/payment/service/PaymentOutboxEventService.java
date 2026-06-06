package com.ticketbooking.payment.service;

import com.ticketbooking.payment.domain.PaymentOutboxEvent;
import com.ticketbooking.payment.repository.PaymentOutboxEventRepository;
import com.ticketbooking.payment.service.dto.PaymentOutboxEventDTO;
import com.ticketbooking.payment.service.mapper.PaymentOutboxEventMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.payment.domain.PaymentOutboxEvent}.
 */
@Service
@Transactional
public class PaymentOutboxEventService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentOutboxEventService.class);

    private final PaymentOutboxEventRepository paymentOutboxEventRepository;

    private final PaymentOutboxEventMapper paymentOutboxEventMapper;

    public PaymentOutboxEventService(
        PaymentOutboxEventRepository paymentOutboxEventRepository,
        PaymentOutboxEventMapper paymentOutboxEventMapper
    ) {
        this.paymentOutboxEventRepository = paymentOutboxEventRepository;
        this.paymentOutboxEventMapper = paymentOutboxEventMapper;
    }

    /**
     * Save a paymentOutboxEvent.
     *
     * @param paymentOutboxEventDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentOutboxEventDTO save(PaymentOutboxEventDTO paymentOutboxEventDTO) {
        LOG.debug("Request to save PaymentOutboxEvent : {}", paymentOutboxEventDTO);
        PaymentOutboxEvent paymentOutboxEvent = paymentOutboxEventMapper.toEntity(paymentOutboxEventDTO);
        paymentOutboxEvent = paymentOutboxEventRepository.save(paymentOutboxEvent);
        return paymentOutboxEventMapper.toDto(paymentOutboxEvent);
    }

    /**
     * Update a paymentOutboxEvent.
     *
     * @param paymentOutboxEventDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentOutboxEventDTO update(PaymentOutboxEventDTO paymentOutboxEventDTO) {
        LOG.debug("Request to update PaymentOutboxEvent : {}", paymentOutboxEventDTO);
        PaymentOutboxEvent paymentOutboxEvent = paymentOutboxEventMapper.toEntity(paymentOutboxEventDTO);
        paymentOutboxEvent = paymentOutboxEventRepository.save(paymentOutboxEvent);
        return paymentOutboxEventMapper.toDto(paymentOutboxEvent);
    }

    /**
     * Partially update a paymentOutboxEvent.
     *
     * @param paymentOutboxEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentOutboxEventDTO> partialUpdate(PaymentOutboxEventDTO paymentOutboxEventDTO) {
        LOG.debug("Request to partially update PaymentOutboxEvent : {}", paymentOutboxEventDTO);

        return paymentOutboxEventRepository
            .findById(paymentOutboxEventDTO.getId())
            .map(existingPaymentOutboxEvent -> {
                paymentOutboxEventMapper.partialUpdate(existingPaymentOutboxEvent, paymentOutboxEventDTO);

                return existingPaymentOutboxEvent;
            })
            .map(paymentOutboxEventRepository::save)
            .map(paymentOutboxEventMapper::toDto);
    }

    /**
     * Get all the paymentOutboxEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentOutboxEventDTO> findAll() {
        LOG.debug("Request to get all PaymentOutboxEvents");
        return paymentOutboxEventRepository
            .findAll()
            .stream()
            .map(paymentOutboxEventMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one paymentOutboxEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentOutboxEventDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentOutboxEvent : {}", id);
        return paymentOutboxEventRepository.findById(id).map(paymentOutboxEventMapper::toDto);
    }

    /**
     * Delete the paymentOutboxEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentOutboxEvent : {}", id);
        paymentOutboxEventRepository.deleteById(id);
    }
}
