import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment-outbox-event.reducer';

export const PaymentOutboxEventDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentOutboxEventEntity = useAppSelector(state => state.gateway.paymentOutboxEvent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentOutboxEventDetailsHeading">
          <Translate contentKey="gatewayApp.paymentServicePaymentOutboxEvent.detail.title">PaymentOutboxEvent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentOutboxEventEntity.id}</dd>
          <dt>
            <span id="aggregateType">
              <Translate contentKey="gatewayApp.paymentServicePaymentOutboxEvent.aggregateType">Aggregate Type</Translate>
            </span>
          </dt>
          <dd>{paymentOutboxEventEntity.aggregateType}</dd>
          <dt>
            <span id="aggregateId">
              <Translate contentKey="gatewayApp.paymentServicePaymentOutboxEvent.aggregateId">Aggregate Id</Translate>
            </span>
          </dt>
          <dd>{paymentOutboxEventEntity.aggregateId}</dd>
          <dt>
            <span id="eventType">
              <Translate contentKey="gatewayApp.paymentServicePaymentOutboxEvent.eventType">Event Type</Translate>
            </span>
          </dt>
          <dd>{paymentOutboxEventEntity.eventType}</dd>
          <dt>
            <span id="payload">
              <Translate contentKey="gatewayApp.paymentServicePaymentOutboxEvent.payload">Payload</Translate>
            </span>
          </dt>
          <dd>{paymentOutboxEventEntity.payload}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.paymentServicePaymentOutboxEvent.status">Status</Translate>
            </span>
          </dt>
          <dd>{paymentOutboxEventEntity.status}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.paymentServicePaymentOutboxEvent.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {paymentOutboxEventEntity.createdAt ? (
              <TextFormat value={paymentOutboxEventEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="publishedAt">
              <Translate contentKey="gatewayApp.paymentServicePaymentOutboxEvent.publishedAt">Published At</Translate>
            </span>
          </dt>
          <dd>
            {paymentOutboxEventEntity.publishedAt ? (
              <TextFormat value={paymentOutboxEventEntity.publishedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="errorMessage">
              <Translate contentKey="gatewayApp.paymentServicePaymentOutboxEvent.errorMessage">Error Message</Translate>
            </span>
          </dt>
          <dd>{paymentOutboxEventEntity.errorMessage}</dd>
        </dl>
        <Button tag={Link} to="/payment-outbox-event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment-outbox-event/${paymentOutboxEventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentOutboxEventDetail;
