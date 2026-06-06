import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ticket.reducer';

export const TicketDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ticketEntity = useAppSelector(state => state.gateway.ticket.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ticketDetailsHeading">
          <Translate contentKey="gatewayApp.ticketServiceTicket.detail.title">Ticket</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.id}</dd>
          <dt>
            <span id="bookingId">
              <Translate contentKey="gatewayApp.ticketServiceTicket.bookingId">Booking Id</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.bookingId}</dd>
          <dt>
            <span id="bookingItemId">
              <Translate contentKey="gatewayApp.ticketServiceTicket.bookingItemId">Booking Item Id</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.bookingItemId}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="gatewayApp.ticketServiceTicket.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.userId}</dd>
          <dt>
            <span id="customerEmail">
              <Translate contentKey="gatewayApp.ticketServiceTicket.customerEmail">Customer Email</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.customerEmail}</dd>
          <dt>
            <span id="eventId">
              <Translate contentKey="gatewayApp.ticketServiceTicket.eventId">Event Id</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.eventId}</dd>
          <dt>
            <span id="eventTitle">
              <Translate contentKey="gatewayApp.ticketServiceTicket.eventTitle">Event Title</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.eventTitle}</dd>
          <dt>
            <span id="ticketTypeId">
              <Translate contentKey="gatewayApp.ticketServiceTicket.ticketTypeId">Ticket Type Id</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.ticketTypeId}</dd>
          <dt>
            <span id="ticketTypeName">
              <Translate contentKey="gatewayApp.ticketServiceTicket.ticketTypeName">Ticket Type Name</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.ticketTypeName}</dd>
          <dt>
            <span id="ticketCode">
              <Translate contentKey="gatewayApp.ticketServiceTicket.ticketCode">Ticket Code</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.ticketCode}</dd>
          <dt>
            <span id="qrPayload">
              <Translate contentKey="gatewayApp.ticketServiceTicket.qrPayload">Qr Payload</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.qrPayload}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.ticketServiceTicket.status">Status</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.status}</dd>
          <dt>
            <span id="issuedAt">
              <Translate contentKey="gatewayApp.ticketServiceTicket.issuedAt">Issued At</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.issuedAt ? <TextFormat value={ticketEntity.issuedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="checkedInAt">
              <Translate contentKey="gatewayApp.ticketServiceTicket.checkedInAt">Checked In At</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.checkedInAt ? <TextFormat value={ticketEntity.checkedInAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.ticketServiceTicket.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.createdAt ? <TextFormat value={ticketEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="gatewayApp.ticketServiceTicket.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{ticketEntity.updatedAt ? <TextFormat value={ticketEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/ticket" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ticket/${ticketEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TicketDetail;
