import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment-webhook-log.reducer';

export const PaymentWebhookLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentWebhookLogEntity = useAppSelector(state => state.gateway.paymentWebhookLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentWebhookLogDetailsHeading">
          <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.detail.title">PaymentWebhookLog</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentWebhookLogEntity.id}</dd>
          <dt>
            <span id="provider">
              <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.provider">Provider</Translate>
            </span>
          </dt>
          <dd>{paymentWebhookLogEntity.provider}</dd>
          <dt>
            <span id="providerEventId">
              <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.providerEventId">Provider Event Id</Translate>
            </span>
          </dt>
          <dd>{paymentWebhookLogEntity.providerEventId}</dd>
          <dt>
            <span id="eventType">
              <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.eventType">Event Type</Translate>
            </span>
          </dt>
          <dd>{paymentWebhookLogEntity.eventType}</dd>
          <dt>
            <span id="rawPayload">
              <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.rawPayload">Raw Payload</Translate>
            </span>
          </dt>
          <dd>{paymentWebhookLogEntity.rawPayload}</dd>
          <dt>
            <span id="signature">
              <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.signature">Signature</Translate>
            </span>
          </dt>
          <dd>{paymentWebhookLogEntity.signature}</dd>
          <dt>
            <span id="processed">
              <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.processed">Processed</Translate>
            </span>
          </dt>
          <dd>{paymentWebhookLogEntity.processed ? 'true' : 'false'}</dd>
          <dt>
            <span id="processingError">
              <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.processingError">Processing Error</Translate>
            </span>
          </dt>
          <dd>{paymentWebhookLogEntity.processingError}</dd>
          <dt>
            <span id="receivedAt">
              <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.receivedAt">Received At</Translate>
            </span>
          </dt>
          <dd>
            {paymentWebhookLogEntity.receivedAt ? (
              <TextFormat value={paymentWebhookLogEntity.receivedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="processedAt">
              <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.processedAt">Processed At</Translate>
            </span>
          </dt>
          <dd>
            {paymentWebhookLogEntity.processedAt ? (
              <TextFormat value={paymentWebhookLogEntity.processedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.payment">Payment</Translate>
          </dt>
          <dd>{paymentWebhookLogEntity.payment ? paymentWebhookLogEntity.payment.id : ''}</dd>
          <dt>
            <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.attempt">Attempt</Translate>
          </dt>
          <dd>{paymentWebhookLogEntity.attempt ? paymentWebhookLogEntity.attempt.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/payment-webhook-log" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment-webhook-log/${paymentWebhookLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentWebhookLogDetail;
