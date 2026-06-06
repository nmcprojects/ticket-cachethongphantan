import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { TicketStatus } from 'app/shared/model/enumerations/ticket-status.model';
import { createEntity, getEntity, reset, updateEntity } from './ticket.reducer';

export const TicketUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ticketEntity = useAppSelector(state => state.gateway.ticket.entity);
  const loading = useAppSelector(state => state.gateway.ticket.loading);
  const updating = useAppSelector(state => state.gateway.ticket.updating);
  const updateSuccess = useAppSelector(state => state.gateway.ticket.updateSuccess);
  const ticketStatusValues = Object.keys(TicketStatus);

  const handleClose = () => {
    navigate(`/ticket${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.bookingItemId !== undefined && typeof values.bookingItemId !== 'number') {
      values.bookingItemId = Number(values.bookingItemId);
    }
    if (values.userId !== undefined && typeof values.userId !== 'number') {
      values.userId = Number(values.userId);
    }
    if (values.eventId !== undefined && typeof values.eventId !== 'number') {
      values.eventId = Number(values.eventId);
    }
    if (values.ticketTypeId !== undefined && typeof values.ticketTypeId !== 'number') {
      values.ticketTypeId = Number(values.ticketTypeId);
    }
    values.issuedAt = convertDateTimeToServer(values.issuedAt);
    values.checkedInAt = convertDateTimeToServer(values.checkedInAt);
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...ticketEntity,
      ...values,
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
          issuedAt: displayDefaultDateTime(),
          checkedInAt: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          status: 'ISSUED',
          ...ticketEntity,
          issuedAt: convertDateTimeFromServer(ticketEntity.issuedAt),
          checkedInAt: convertDateTimeFromServer(ticketEntity.checkedInAt),
          createdAt: convertDateTimeFromServer(ticketEntity.createdAt),
          updatedAt: convertDateTimeFromServer(ticketEntity.updatedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.ticketServiceTicket.home.createOrEditLabel" data-cy="TicketCreateUpdateHeading">
            <Translate contentKey="gatewayApp.ticketServiceTicket.home.createOrEditLabel">Create or edit a Ticket</Translate>
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
                  id="ticket-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.bookingId')}
                id="ticket-bookingId"
                name="bookingId"
                data-cy="bookingId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.bookingItemId')}
                id="ticket-bookingItemId"
                name="bookingItemId"
                data-cy="bookingItemId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.userId')}
                id="ticket-userId"
                name="userId"
                data-cy="userId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.customerEmail')}
                id="ticket-customerEmail"
                name="customerEmail"
                data-cy="customerEmail"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.eventId')}
                id="ticket-eventId"
                name="eventId"
                data-cy="eventId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.eventTitle')}
                id="ticket-eventTitle"
                name="eventTitle"
                data-cy="eventTitle"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.ticketTypeId')}
                id="ticket-ticketTypeId"
                name="ticketTypeId"
                data-cy="ticketTypeId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.ticketTypeName')}
                id="ticket-ticketTypeName"
                name="ticketTypeName"
                data-cy="ticketTypeName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.ticketCode')}
                id="ticket-ticketCode"
                name="ticketCode"
                data-cy="ticketCode"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.qrPayload')}
                id="ticket-qrPayload"
                name="qrPayload"
                data-cy="qrPayload"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 2048, message: translate('entity.validation.maxlength', { max: 2048 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.status')}
                id="ticket-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {ticketStatusValues.map(ticketStatus => (
                  <option value={ticketStatus} key={ticketStatus}>
                    {translate(`gatewayApp.TicketStatus.${ticketStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.issuedAt')}
                id="ticket-issuedAt"
                name="issuedAt"
                data-cy="issuedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.checkedInAt')}
                id="ticket-checkedInAt"
                name="checkedInAt"
                data-cy="checkedInAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.createdAt')}
                id="ticket-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicket.updatedAt')}
                id="ticket-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ticket" replace color="info">
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

export default TicketUpdate;
