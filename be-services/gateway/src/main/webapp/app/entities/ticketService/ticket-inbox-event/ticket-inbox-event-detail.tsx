import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ticket-inbox-event.reducer';

export const TicketInboxEventDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ticketInboxEventEntity = useAppSelector(state => state.gateway.ticketInboxEvent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ticketInboxEventDetailsHeading">
          <Translate contentKey="gatewayApp.ticketServiceTicketInboxEvent.detail.title">TicketInboxEvent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ticketInboxEventEntity.id}</dd>
          <dt>
            <span id="sourceService">
              <Translate contentKey="gatewayApp.ticketServiceTicketInboxEvent.sourceService">Source Service</Translate>
            </span>
          </dt>
          <dd>{ticketInboxEventEntity.sourceService}</dd>
          <dt>
            <span id="eventId">
              <Translate contentKey="gatewayApp.ticketServiceTicketInboxEvent.eventId">Event Id</Translate>
            </span>
          </dt>
          <dd>{ticketInboxEventEntity.eventId}</dd>
          <dt>
            <span id="eventType">
              <Translate contentKey="gatewayApp.ticketServiceTicketInboxEvent.eventType">Event Type</Translate>
            </span>
          </dt>
          <dd>{ticketInboxEventEntity.eventType}</dd>
          <dt>
            <span id="payload">
              <Translate contentKey="gatewayApp.ticketServiceTicketInboxEvent.payload">Payload</Translate>
            </span>
          </dt>
          <dd>{ticketInboxEventEntity.payload}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.ticketServiceTicketInboxEvent.status">Status</Translate>
            </span>
          </dt>
          <dd>{ticketInboxEventEntity.status}</dd>
          <dt>
            <span id="receivedAt">
              <Translate contentKey="gatewayApp.ticketServiceTicketInboxEvent.receivedAt">Received At</Translate>
            </span>
          </dt>
          <dd>
            {ticketInboxEventEntity.receivedAt ? (
              <TextFormat value={ticketInboxEventEntity.receivedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="processedAt">
              <Translate contentKey="gatewayApp.ticketServiceTicketInboxEvent.processedAt">Processed At</Translate>
            </span>
          </dt>
          <dd>
            {ticketInboxEventEntity.processedAt ? (
              <TextFormat value={ticketInboxEventEntity.processedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="errorMessage">
              <Translate contentKey="gatewayApp.ticketServiceTicketInboxEvent.errorMessage">Error Message</Translate>
            </span>
          </dt>
          <dd>{ticketInboxEventEntity.errorMessage}</dd>
        </dl>
        <Button tag={Link} to="/ticket-inbox-event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ticket-inbox-event/${ticketInboxEventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TicketInboxEventDetail;
