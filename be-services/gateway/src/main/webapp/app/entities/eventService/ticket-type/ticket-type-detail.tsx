import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ticket-type.reducer';

export const TicketTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ticketTypeEntity = useAppSelector(state => state.gateway.ticketType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ticketTypeDetailsHeading">
          <Translate contentKey="gatewayApp.eventServiceTicketType.detail.title">TicketType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="gatewayApp.eventServiceTicketType.name">Name</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="gatewayApp.eventServiceTicketType.description">Description</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.description}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="gatewayApp.eventServiceTicketType.price">Price</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.price}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="gatewayApp.eventServiceTicketType.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.currency}</dd>
          <dt>
            <span id="totalQuantity">
              <Translate contentKey="gatewayApp.eventServiceTicketType.totalQuantity">Total Quantity</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.totalQuantity}</dd>
          <dt>
            <span id="availableQuantity">
              <Translate contentKey="gatewayApp.eventServiceTicketType.availableQuantity">Available Quantity</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.availableQuantity}</dd>
          <dt>
            <span id="reservedQuantity">
              <Translate contentKey="gatewayApp.eventServiceTicketType.reservedQuantity">Reserved Quantity</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.reservedQuantity}</dd>
          <dt>
            <span id="soldQuantity">
              <Translate contentKey="gatewayApp.eventServiceTicketType.soldQuantity">Sold Quantity</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.soldQuantity}</dd>
          <dt>
            <span id="maxPerOrder">
              <Translate contentKey="gatewayApp.eventServiceTicketType.maxPerOrder">Max Per Order</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.maxPerOrder}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.eventServiceTicketType.status">Status</Translate>
            </span>
          </dt>
          <dd>{ticketTypeEntity.status}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.eventServiceTicketType.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {ticketTypeEntity.createdAt ? <TextFormat value={ticketTypeEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="gatewayApp.eventServiceTicketType.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {ticketTypeEntity.updatedAt ? <TextFormat value={ticketTypeEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="gatewayApp.eventServiceTicketType.event">Event</Translate>
          </dt>
          <dd>{ticketTypeEntity.event ? ticketTypeEntity.event.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ticket-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ticket-type/${ticketTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TicketTypeDetail;
