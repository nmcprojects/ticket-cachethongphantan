package com.ticketbooking.payment.service;

import com.ticketbooking.payment.domain.PaymentWebhookLog;
import com.ticketbooking.payment.repository.PaymentWebhookLogRepository;
import com.ticketbooking.payment.service.dto.PaymentWebhookLogDTO;
import com.ticketbooking.payment.service.mapper.PaymentWebhookLogMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ticketbooking.payment.domain.PaymentWebhookLog}.
 */
@Service
@Transactional
public class PaymentWebhookLogService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentWebhookLogService.class);

    private final PaymentWebhookLogRepository paymentWebhookLogRepository;

    private final PaymentWebhookLogMapper paymentWebhookLogMapper;

    public PaymentWebhookLogService(
        PaymentWebhookLogRepository paymentWebhookLogRepository,
        PaymentWebhookLogMapper paymentWebhookLogMapper
    ) {
        this.paymentWebhookLogRepository = paymentWebhookLogRepository;
        this.paymentWebhookLogMapper = paymentWebhookLogMapper;
    }

    /**
     * Save a paymentWebhookLog.
     *
     * @param paymentWebhookLogDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentWebhookLogDTO save(PaymentWebhookLogDTO paymentWebhookLogDTO) {
        LOG.debug("Request to save PaymentWebhookLog : {}", paymentWebhookLogDTO);
        PaymentWebhookLog paymentWebhookLog = paymentWebhookLogMapper.toEntity(paymentWebhookLogDTO);
        paymentWebhookLog = paymentWebhookLogRepository.save(paymentWebhookLog);
        return paymentWebhookLogMapper.toDto(paymentWebhookLog);
    }

    /**
     * Update a paymentWebhookLog.
     *
     * @param paymentWebhookLogDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentWebhookLogDTO update(PaymentWebhookLogDTO paymentWebhookLogDTO) {
        LOG.debug("Request to update PaymentWebhookLog : {}", paymentWebhookLogDTO);
        PaymentWebhookLog paymentWebhookLog = paymentWebhookLogMapper.toEntity(paymentWebhookLogDTO);
        paymentWebhookLog = paymentWebhookLogRepository.save(paymentWebhookLog);
        return paymentWebhookLogMapper.toDto(paymentWebhookLog);
    }

    /**
     * Partially update a paymentWebhookLog.
     *
     * @param paymentWebhookLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentWebhookLogDTO> partialUpdate(PaymentWebhookLogDTO paymentWebhookLogDTO) {
        LOG.debug("Request to partially update PaymentWebhookLog : {}", paymentWebhookLogDTO);

        return paymentWebhookLogRepository
            .findById(paymentWebhookLogDTO.getId())
            .map(existingPaymentWebhookLog -> {
                paymentWebhookLogMapper.partialUpdate(existingPaymentWebhookLog, paymentWebhookLogDTO);

                return existingPaymentWebhookLog;
            })
            .map(paymentWebhookLogRepository::save)
            .map(paymentWebhookLogMapper::toDto);
    }

    /**
     * Get all the paymentWebhookLogs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentWebhookLogDTO> findAll() {
        LOG.debug("Request to get all PaymentWebhookLogs");
        return paymentWebhookLogRepository
            .findAll()
            .stream()
            .map(paymentWebhookLogMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one paymentWebhookLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentWebhookLogDTO> findOne(Long id) {
        LOG.debug("Request to get PaymentWebhookLog : {}", id);
        return paymentWebhookLogRepository.findById(id).map(paymentWebhookLogMapper::toDto);
    }

    /**
     * Delete the paymentWebhookLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PaymentWebhookLog : {}", id);
        paymentWebhookLogRepository.deleteById(id);
    }
}
