import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './booking-status-history.reducer';

export const BookingStatusHistoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bookingStatusHistoryEntity = useAppSelector(state => state.gateway.bookingStatusHistory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookingStatusHistoryDetailsHeading">
          <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.detail.title">BookingStatusHistory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bookingStatusHistoryEntity.id}</dd>
          <dt>
            <span id="oldStatus">
              <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.oldStatus">Old Status</Translate>
            </span>
          </dt>
          <dd>{bookingStatusHistoryEntity.oldStatus}</dd>
          <dt>
            <span id="newStatus">
              <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.newStatus">New Status</Translate>
            </span>
          </dt>
          <dd>{bookingStatusHistoryEntity.newStatus}</dd>
          <dt>
            <span id="reason">
              <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.reason">Reason</Translate>
            </span>
          </dt>
          <dd>{bookingStatusHistoryEntity.reason}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {bookingStatusHistoryEntity.createdAt ? (
              <TextFormat value={bookingStatusHistoryEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.booking">Booking</Translate>
          </dt>
          <dd>{bookingStatusHistoryEntity.booking ? bookingStatusHistoryEntity.booking.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/booking-status-history" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/booking-status-history/${bookingStatusHistoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BookingStatusHistoryDetail;
