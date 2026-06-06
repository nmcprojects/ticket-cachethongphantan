import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './booking-outbox-event.reducer';

export const BookingOutboxEventDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bookingOutboxEventEntity = useAppSelector(state => state.gateway.bookingOutboxEvent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookingOutboxEventDetailsHeading">
          <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.detail.title">BookingOutboxEvent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bookingOutboxEventEntity.id}</dd>
          <dt>
            <span id="aggregateType">
              <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.aggregateType">Aggregate Type</Translate>
            </span>
          </dt>
          <dd>{bookingOutboxEventEntity.aggregateType}</dd>
          <dt>
            <span id="aggregateId">
              <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.aggregateId">Aggregate Id</Translate>
            </span>
          </dt>
          <dd>{bookingOutboxEventEntity.aggregateId}</dd>
          <dt>
            <span id="eventType">
              <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.eventType">Event Type</Translate>
            </span>
          </dt>
          <dd>{bookingOutboxEventEntity.eventType}</dd>
          <dt>
            <span id="payload">
              <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.payload">Payload</Translate>
            </span>
          </dt>
          <dd>{bookingOutboxEventEntity.payload}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.status">Status</Translate>
            </span>
          </dt>
          <dd>{bookingOutboxEventEntity.status}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {bookingOutboxEventEntity.createdAt ? (
              <TextFormat value={bookingOutboxEventEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="publishedAt">
              <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.publishedAt">Published At</Translate>
            </span>
          </dt>
          <dd>
            {bookingOutboxEventEntity.publishedAt ? (
              <TextFormat value={bookingOutboxEventEntity.publishedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="errorMessage">
              <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.errorMessage">Error Message</Translate>
            </span>
          </dt>
          <dd>{bookingOutboxEventEntity.errorMessage}</dd>
        </dl>
        <Button tag={Link} to="/booking-outbox-event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/booking-outbox-event/${bookingOutboxEventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BookingOutboxEventDetail;
