import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './booking-inbox-event.reducer';

export const BookingInboxEventDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bookingInboxEventEntity = useAppSelector(state => state.gateway.bookingInboxEvent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookingInboxEventDetailsHeading">
          <Translate contentKey="gatewayApp.bookingServiceBookingInboxEvent.detail.title">BookingInboxEvent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bookingInboxEventEntity.id}</dd>
          <dt>
            <span id="sourceService">
              <Translate contentKey="gatewayApp.bookingServiceBookingInboxEvent.sourceService">Source Service</Translate>
            </span>
          </dt>
          <dd>{bookingInboxEventEntity.sourceService}</dd>
          <dt>
            <span id="eventId">
              <Translate contentKey="gatewayApp.bookingServiceBookingInboxEvent.eventId">Event Id</Translate>
            </span>
          </dt>
          <dd>{bookingInboxEventEntity.eventId}</dd>
          <dt>
            <span id="eventType">
              <Translate contentKey="gatewayApp.bookingServiceBookingInboxEvent.eventType">Event Type</Translate>
            </span>
          </dt>
          <dd>{bookingInboxEventEntity.eventType}</dd>
          <dt>
            <span id="payload">
              <Translate contentKey="gatewayApp.bookingServiceBookingInboxEvent.payload">Payload</Translate>
            </span>
          </dt>
          <dd>{bookingInboxEventEntity.payload}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.bookingServiceBookingInboxEvent.status">Status</Translate>
            </span>
          </dt>
          <dd>{bookingInboxEventEntity.status}</dd>
          <dt>
            <span id="receivedAt">
              <Translate contentKey="gatewayApp.bookingServiceBookingInboxEvent.receivedAt">Received At</Translate>
            </span>
          </dt>
          <dd>
            {bookingInboxEventEntity.receivedAt ? (
              <TextFormat value={bookingInboxEventEntity.receivedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="processedAt">
              <Translate contentKey="gatewayApp.bookingServiceBookingInboxEvent.processedAt">Processed At</Translate>
            </span>
          </dt>
          <dd>
            {bookingInboxEventEntity.processedAt ? (
              <TextFormat value={bookingInboxEventEntity.processedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="errorMessage">
              <Translate contentKey="gatewayApp.bookingServiceBookingInboxEvent.errorMessage">Error Message</Translate>
            </span>
          </dt>
          <dd>{bookingInboxEventEntity.errorMessage}</dd>
        </dl>
        <Button tag={Link} to="/booking-inbox-event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/booking-inbox-event/${bookingInboxEventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BookingInboxEventDetail;
