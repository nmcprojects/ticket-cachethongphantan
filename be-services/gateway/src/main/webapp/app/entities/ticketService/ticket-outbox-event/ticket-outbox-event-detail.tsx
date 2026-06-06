import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ticket-outbox-event.reducer';

export const TicketOutboxEventDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ticketOutboxEventEntity = useAppSelector(state => state.gateway.ticketOutboxEvent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ticketOutboxEventDetailsHeading">
          <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.detail.title">TicketOutboxEvent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ticketOutboxEventEntity.id}</dd>
          <dt>
            <span id="aggregateType">
              <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.aggregateType">Aggregate Type</Translate>
            </span>
          </dt>
          <dd>{ticketOutboxEventEntity.aggregateType}</dd>
          <dt>
            <span id="aggregateId">
              <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.aggregateId">Aggregate Id</Translate>
            </span>
          </dt>
          <dd>{ticketOutboxEventEntity.aggregateId}</dd>
          <dt>
            <span id="eventType">
              <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.eventType">Event Type</Translate>
            </span>
          </dt>
          <dd>{ticketOutboxEventEntity.eventType}</dd>
          <dt>
            <span id="payload">
              <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.payload">Payload</Translate>
            </span>
          </dt>
          <dd>{ticketOutboxEventEntity.payload}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.status">Status</Translate>
            </span>
          </dt>
          <dd>{ticketOutboxEventEntity.status}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {ticketOutboxEventEntity.createdAt ? (
              <TextFormat value={ticketOutboxEventEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="publishedAt">
              <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.publishedAt">Published At</Translate>
            </span>
          </dt>
          <dd>
            {ticketOutboxEventEntity.publishedAt ? (
              <TextFormat value={ticketOutboxEventEntity.publishedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="errorMessage">
              <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.errorMessage">Error Message</Translate>
            </span>
          </dt>
          <dd>{ticketOutboxEventEntity.errorMessage}</dd>
        </dl>
        <Button tag={Link} to="/ticket-outbox-event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ticket-outbox-event/${ticketOutboxEventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TicketOutboxEventDetail;
