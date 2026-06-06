import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getBookings } from 'app/entities/bookingService/booking/booking.reducer';
import { BookingStatus } from 'app/shared/model/enumerations/booking-status.model';
import { createEntity, getEntity, reset, updateEntity } from './booking-status-history.reducer';

export const BookingStatusHistoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bookings = useAppSelector(state => state.gateway.booking.entities);
  const bookingStatusHistoryEntity = useAppSelector(state => state.gateway.bookingStatusHistory.entity);
  const loading = useAppSelector(state => state.gateway.bookingStatusHistory.loading);
  const updating = useAppSelector(state => state.gateway.bookingStatusHistory.updating);
  const updateSuccess = useAppSelector(state => state.gateway.bookingStatusHistory.updateSuccess);
  const bookingStatusValues = Object.keys(BookingStatus);

  const handleClose = () => {
    navigate('/booking-status-history');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBookings({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...bookingStatusHistoryEntity,
      ...values,
      booking: bookings.find(it => it.id.toString() === values.booking?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
        }
      : {
          oldStatus: 'PENDING_PAYMENT',
          newStatus: 'PENDING_PAYMENT',
          ...bookingStatusHistoryEntity,
          createdAt: convertDateTimeFromServer(bookingStatusHistoryEntity.createdAt),
          booking: bookingStatusHistoryEntity?.booking?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.bookingServiceBookingStatusHistory.home.createOrEditLabel" data-cy="BookingStatusHistoryCreateUpdateHeading">
            <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.home.createOrEditLabel">
              Create or edit a BookingStatusHistory
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="booking-status-history-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.bookingServiceBookingStatusHistory.oldStatus')}
                id="booking-status-history-oldStatus"
                name="oldStatus"
                data-cy="oldStatus"
                type="select"
              >
                {bookingStatusValues.map(bookingStatus => (
                  <option value={bookingStatus} key={bookingStatus}>
                    {translate(`gatewayApp.BookingStatus.${bookingStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.bookingServiceBookingStatusHistory.newStatus')}
                id="booking-status-history-newStatus"
                name="newStatus"
                data-cy="newStatus"
                type="select"
              >
                {bookingStatusValues.map(bookingStatus => (
                  <option value={bookingStatus} key={bookingStatus}>
                    {translate(`gatewayApp.BookingStatus.${bookingStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.bookingServiceBookingStatusHistory.reason')}
                id="booking-status-history-reason"
                name="reason"
                data-cy="reason"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.bookingServiceBookingStatusHistory.createdAt')}
                id="booking-status-history-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="booking-status-history-booking"
                name="booking"
                data-cy="booking"
                label={translate('gatewayApp.bookingServiceBookingStatusHistory.booking')}
                type="select"
                required
              >
                <option value="" key="0" />
                {bookings
                  ? bookings.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/booking-status-history" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BookingStatusHistoryUpdate;
