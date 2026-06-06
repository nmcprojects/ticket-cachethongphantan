import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTicketTypes } from 'app/entities/eventService/ticket-type/ticket-type.reducer';
import { ReservationStatus } from 'app/shared/model/enumerations/reservation-status.model';
import { createEntity, getEntity, reset, updateEntity } from './ticket-reservation.reducer';

export const TicketReservationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ticketTypes = useAppSelector(state => state.gateway.ticketType.entities);
  const ticketReservationEntity = useAppSelector(state => state.gateway.ticketReservation.entity);
  const loading = useAppSelector(state => state.gateway.ticketReservation.loading);
  const updating = useAppSelector(state => state.gateway.ticketReservation.updating);
  const updateSuccess = useAppSelector(state => state.gateway.ticketReservation.updateSuccess);
  const reservationStatusValues = Object.keys(ReservationStatus);

  const handleClose = () => {
    navigate('/ticket-reservation');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTicketTypes({}));
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
    if (values.bookingId !== undefined && typeof values.bookingId !== 'number') {
      values.bookingId = Number(values.bookingId);
    }
    if (values.userId !== undefined && typeof values.userId !== 'number') {
      values.userId = Number(values.userId);
    }
    if (values.quantity !== undefined && typeof values.quantity !== 'number') {
      values.quantity = Number(values.quantity);
    }
    values.expiresAt = convertDateTimeToServer(values.expiresAt);
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...ticketReservationEntity,
      ...values,
      ticketType: ticketTypes.find(it => it.id.toString() === values.ticketType?.toString()),
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
          expiresAt: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          status: 'HELD',
          ...ticketReservationEntity,
          expiresAt: convertDateTimeFromServer(ticketReservationEntity.expiresAt),
          createdAt: convertDateTimeFromServer(ticketReservationEntity.createdAt),
          updatedAt: convertDateTimeFromServer(ticketReservationEntity.updatedAt),
          ticketType: ticketReservationEntity?.ticketType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.eventServiceTicketReservation.home.createOrEditLabel" data-cy="TicketReservationCreateUpdateHeading">
            <Translate contentKey="gatewayApp.eventServiceTicketReservation.home.createOrEditLabel">
              Create or edit a TicketReservation
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
                  id="ticket-reservation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketReservation.bookingId')}
                id="ticket-reservation-bookingId"
                name="bookingId"
                data-cy="bookingId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketReservation.userId')}
                id="ticket-reservation-userId"
                name="userId"
                data-cy="userId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketReservation.quantity')}
                id="ticket-reservation-quantity"
                name="quantity"
                data-cy="quantity"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketReservation.status')}
                id="ticket-reservation-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {reservationStatusValues.map(reservationStatus => (
                  <option value={reservationStatus} key={reservationStatus}>
                    {translate(`gatewayApp.ReservationStatus.${reservationStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketReservation.expiresAt')}
                id="ticket-reservation-expiresAt"
                name="expiresAt"
                data-cy="expiresAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketReservation.createdAt')}
                id="ticket-reservation-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketReservation.updatedAt')}
                id="ticket-reservation-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="ticket-reservation-ticketType"
                name="ticketType"
                data-cy="ticketType"
                label={translate('gatewayApp.eventServiceTicketReservation.ticketType')}
                type="select"
                required
              >
                <option value="" key="0" />
                {ticketTypes
                  ? ticketTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ticket-reservation" replace color="info">
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

export default TicketReservationUpdate;
