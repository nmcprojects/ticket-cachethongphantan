import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ticket-reservation.reducer';

export const TicketReservationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ticketReservationEntity = useAppSelector(state => state.gateway.ticketReservation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ticketReservationDetailsHeading">
          <Translate contentKey="gatewayApp.eventServiceTicketReservation.detail.title">TicketReservation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ticketReservationEntity.id}</dd>
          <dt>
            <span id="bookingId">
              <Translate contentKey="gatewayApp.eventServiceTicketReservation.bookingId">Booking Id</Translate>
            </span>
          </dt>
          <dd>{ticketReservationEntity.bookingId}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="gatewayApp.eventServiceTicketReservation.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{ticketReservationEntity.userId}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="gatewayApp.eventServiceTicketReservation.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{ticketReservationEntity.quantity}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.eventServiceTicketReservation.status">Status</Translate>
            </span>
          </dt>
          <dd>{ticketReservationEntity.status}</dd>
          <dt>
            <span id="expiresAt">
              <Translate contentKey="gatewayApp.eventServiceTicketReservation.expiresAt">Expires At</Translate>
            </span>
          </dt>
          <dd>
            {ticketReservationEntity.expiresAt ? (
              <TextFormat value={ticketReservationEntity.expiresAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.eventServiceTicketReservation.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {ticketReservationEntity.createdAt ? (
              <TextFormat value={ticketReservationEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="gatewayApp.eventServiceTicketReservation.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {ticketReservationEntity.updatedAt ? (
              <TextFormat value={ticketReservationEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="gatewayApp.eventServiceTicketReservation.ticketType">Ticket Type</Translate>
          </dt>
          <dd>{ticketReservationEntity.ticketType ? ticketReservationEntity.ticketType.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ticket-reservation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ticket-reservation/${ticketReservationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TicketReservationDetail;
