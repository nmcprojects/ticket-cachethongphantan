import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './booking.reducer';

export const BookingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bookingEntity = useAppSelector(state => state.gateway.booking.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookingDetailsHeading">
          <Translate contentKey="gatewayApp.bookingServiceBooking.detail.title">Booking</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.id}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="gatewayApp.bookingServiceBooking.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.userId}</dd>
          <dt>
            <span id="customerEmail">
              <Translate contentKey="gatewayApp.bookingServiceBooking.customerEmail">Customer Email</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.customerEmail}</dd>
          <dt>
            <span id="eventId">
              <Translate contentKey="gatewayApp.bookingServiceBooking.eventId">Event Id</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.eventId}</dd>
          <dt>
            <span id="eventTitle">
              <Translate contentKey="gatewayApp.bookingServiceBooking.eventTitle">Event Title</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.eventTitle}</dd>
          <dt>
            <span id="totalAmount">
              <Translate contentKey="gatewayApp.bookingServiceBooking.totalAmount">Total Amount</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.totalAmount}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="gatewayApp.bookingServiceBooking.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.currency}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.bookingServiceBooking.status">Status</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.status}</dd>
          <dt>
            <span id="paymentId">
              <Translate contentKey="gatewayApp.bookingServiceBooking.paymentId">Payment Id</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.paymentId}</dd>
          <dt>
            <span id="paymentUrl">
              <Translate contentKey="gatewayApp.bookingServiceBooking.paymentUrl">Payment Url</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.paymentUrl}</dd>
          <dt>
            <span id="expiredAt">
              <Translate contentKey="gatewayApp.bookingServiceBooking.expiredAt">Expired At</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.expiredAt ? <TextFormat value={bookingEntity.expiredAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="paidAt">
              <Translate contentKey="gatewayApp.bookingServiceBooking.paidAt">Paid At</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.paidAt ? <TextFormat value={bookingEntity.paidAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.bookingServiceBooking.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.createdAt ? <TextFormat value={bookingEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="gatewayApp.bookingServiceBooking.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.updatedAt ? <TextFormat value={bookingEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/booking" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/booking/${bookingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BookingDetail;
