import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './booking-item.reducer';

export const BookingItemDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bookingItemEntity = useAppSelector(state => state.gateway.bookingItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookingItemDetailsHeading">
          <Translate contentKey="gatewayApp.bookingServiceBookingItem.detail.title">BookingItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bookingItemEntity.id}</dd>
          <dt>
            <span id="ticketTypeId">
              <Translate contentKey="gatewayApp.bookingServiceBookingItem.ticketTypeId">Ticket Type Id</Translate>
            </span>
          </dt>
          <dd>{bookingItemEntity.ticketTypeId}</dd>
          <dt>
            <span id="ticketTypeName">
              <Translate contentKey="gatewayApp.bookingServiceBookingItem.ticketTypeName">Ticket Type Name</Translate>
            </span>
          </dt>
          <dd>{bookingItemEntity.ticketTypeName}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="gatewayApp.bookingServiceBookingItem.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{bookingItemEntity.quantity}</dd>
          <dt>
            <span id="unitPrice">
              <Translate contentKey="gatewayApp.bookingServiceBookingItem.unitPrice">Unit Price</Translate>
            </span>
          </dt>
          <dd>{bookingItemEntity.unitPrice}</dd>
          <dt>
            <span id="totalPrice">
              <Translate contentKey="gatewayApp.bookingServiceBookingItem.totalPrice">Total Price</Translate>
            </span>
          </dt>
          <dd>{bookingItemEntity.totalPrice}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.bookingServiceBookingItem.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {bookingItemEntity.createdAt ? <TextFormat value={bookingItemEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="gatewayApp.bookingServiceBookingItem.booking">Booking</Translate>
          </dt>
          <dd>{bookingItemEntity.booking ? bookingItemEntity.booking.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/booking-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/booking-item/${bookingItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BookingItemDetail;
