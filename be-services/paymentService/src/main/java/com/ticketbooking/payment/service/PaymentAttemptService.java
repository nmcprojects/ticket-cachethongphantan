package com.ticketbooking.payment.service;

import com.ticketbooking.payment.domain.PaymentAttempt;
import com.ticketbooking.payment.repository.PaymentAttemptRepository;
import com.ticketbooking.payment.service.dto.PaymentAttemptDTO;
import com.ticketbooking.payment.service.mapper.PaymentAttemptMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.payment.domain.PaymentAttempt}.
 */
@Service
@Transactional
public class PaymentAttemptService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentAttemptService.class);

    private final PaymentAttemptRepository paymentAttemptRepository;

    private final PaymentAttemptMapper paymentAttemptMapper;

    public PaymentAttemptService(PaymentAttemptRepository paymentAttemptRepository, PaymentAttemptMapper paymentAttemptMapper) {
        this.paymentAttemptRepository = paymentAttemptRepository;
        this.paymentAttemptMapper = paymentAttemptMapper;
    }

    /**
     * Save a paymentAttempt.
     *
     * @param paymentAttemptDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentAttemptDTO save(PaymentAttemptDTO paymentAttemptDTO) {
        LOG.debug("Request to save PaymentAttempt : {}", paymentAttemptDTO);
        PaymentAttempt paymentAttempt = paymentAttemptMapper.toEntity(paymentAttemptDTO);
        paymentAttempt = paymentAttemptRepository.save(paymentAttempt);
        return paymentAttemptMapper.toDto(paymentAttempt);
    }

    /**
     * Update a paymentAttempt.
     *
     * @param paymentAttemptDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentAttemptDTO update(PaymentAttemptDTO paymentAttemptDTO) {
        LOG.debug("Request to update PaymentAttempt : {}", paymentAttemptDTO);
        PaymentAttempt paymentAttempt = paymentAttemptMapper.toEntity(paymentAttemptDTO);
        paymentAttempt = paymentAttemptRepository.save(paymentAttempt);
        return paymentAttemptMapper.toDto(paymentAttempt);
    }

    /**
     * Partially update a paymentAttempt.
     *
     * @param paymentAttemptDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentAttemptDTO> partialUpdate(PaymentAttemptDTO paymentAttemptDTO) {
        LOG.debug("Request to partially update PaymentAttempt : {}", paymentAttemptDTO);

        return paymentAttemptRepository
            .findById(paymentAttemptDTO.getId())
            .map(existingPaymentAttempt -> {
                paymentAttemptMapper.partialUpdate(existingPaymentAttempt, paymentAttemptDTO);

                return existingPaymentAttempt;
            })
            .map(paymentAttemptRepository::save)
            .map(paymentAttemptMapper::toDto);
    }

    /**
     * Get all the paymentAttempts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentAttemptDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PaymentAttempts");
        return paymentAttemptRepository.findAll(pageable).map(paymentAttemptMapper::toDto);
    }

    /**
     * Get one paymentAttempt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentAttemptDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentAttempt : {}", id);
        return paymentAttemptRepository.findById(id).map(paymentAttemptMapper::toDto);
    }

    /**
     * Delete the paymentAttempt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentAttempt : {}", id);
        paymentAttemptRepository.deleteById(id);
    }
}
