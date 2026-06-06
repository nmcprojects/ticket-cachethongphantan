import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPayments } from 'app/entities/paymentService/payment/payment.reducer';
import { getEntities as getPaymentAttempts } from 'app/entities/paymentService/payment-attempt/payment-attempt.reducer';
import { PaymentProvider } from 'app/shared/model/enumerations/payment-provider.model';
import { createEntity, getEntity, reset, updateEntity } from './payment-webhook-log.reducer';

export const PaymentWebhookLogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const payments = useAppSelector(state => state.gateway.payment.entities);
  const paymentAttempts = useAppSelector(state => state.gateway.paymentAttempt.entities);
  const paymentWebhookLogEntity = useAppSelector(state => state.gateway.paymentWebhookLog.entity);
  const loading = useAppSelector(state => state.gateway.paymentWebhookLog.loading);
  const updating = useAppSelector(state => state.gateway.paymentWebhookLog.updating);
  const updateSuccess = useAppSelector(state => state.gateway.paymentWebhookLog.updateSuccess);
  const paymentProviderValues = Object.keys(PaymentProvider);

  const handleClose = () => {
    navigate('/payment-webhook-log');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPayments({}));
    dispatch(getPaymentAttempts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.receivedAt = convertDateTimeToServer(values.receivedAt);
    values.processedAt = convertDateTimeToServer(values.processedAt);

    const entity = {
      ...paymentWebhookLogEntity,
      ...values,
      payment: payments.find(it => it.id.toString() === values.payment?.toString()),
      attempt: paymentAttempts.find(it => it.id.toString() === values.attempt?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          receivedAt: displayDefaultDateTime(),
          processedAt: displayDefaultDateTime(),
        }
      : {
          provider: 'STRIPE',
          ...paymentWebhookLogEntity,
          receivedAt: convertDateTimeFromServer(paymentWebhookLogEntity.receivedAt),
          processedAt: convertDateTimeFromServer(paymentWebhookLogEntity.processedAt),
          payment: paymentWebhookLogEntity?.payment?.id,
          attempt: paymentWebhookLogEntity?.attempt?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.paymentServicePaymentWebhookLog.home.createOrEditLabel" data-cy="PaymentWebhookLogCreateUpdateHeading">
            <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.home.createOrEditLabel">
              Create or edit a PaymentWebhookLog
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="payment-webhook-log-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentWebhookLog.provider')}
                id="payment-webhook-log-provider"
                name="provider"
                data-cy="provider"
                type="select"
              >
                {paymentProviderValues.map(paymentProvider => (
                  <option value={paymentProvider} key={paymentProvider}>
                    {translate(`gatewayApp.PaymentProvider.${paymentProvider}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentWebhookLog.providerEventId')}
                id="payment-webhook-log-providerEventId"
                name="providerEventId"
                data-cy="providerEventId"
                type="text"
                validate={{
                  maxLength: { value: 512, message: translate('entity.validation.maxlength', { max: 512 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentWebhookLog.eventType')}
                id="payment-webhook-log-eventType"
                name="eventType"
                data-cy="eventType"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentWebhookLog.rawPayload')}
                id="payment-webhook-log-rawPayload"
                name="rawPayload"
                data-cy="rawPayload"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentWebhookLog.signature')}
                id="payment-webhook-log-signature"
                name="signature"
                data-cy="signature"
                type="text"
                validate={{
                  maxLength: { value: 1024, message: translate('entity.validation.maxlength', { max: 1024 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentWebhookLog.processed')}
                id="payment-webhook-log-processed"
                name="processed"
                data-cy="processed"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentWebhookLog.processingError')}
                id="payment-webhook-log-processingError"
                name="processingError"
                data-cy="processingError"
                type="text"
                validate={{
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentWebhookLog.receivedAt')}
                id="payment-webhook-log-receivedAt"
                name="receivedAt"
                data-cy="receivedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentWebhookLog.processedAt')}
                id="payment-webhook-log-processedAt"
                name="processedAt"
                data-cy="processedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="payment-webhook-log-payment"
                name="payment"
                data-cy="payment"
                label={translate('gatewayApp.paymentServicePaymentWebhookLog.payment')}
                type="select"
              >
                <option value="" key="0" />
                {payments
                  ? payments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="payment-webhook-log-attempt"
                name="attempt"
                data-cy="attempt"
                label={translate('gatewayApp.paymentServicePaymentWebhookLog.attempt')}
                type="select"
              >
                <option value="" key="0" />
                {paymentAttempts
                  ? paymentAttempts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/payment-webhook-log" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PaymentWebhookLogUpdate;
