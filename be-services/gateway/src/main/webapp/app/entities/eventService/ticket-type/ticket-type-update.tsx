import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getEvents } from 'app/entities/eventService/event/event.reducer';
import { TicketTypeStatus } from 'app/shared/model/enumerations/ticket-type-status.model';
import { createEntity, getEntity, reset, updateEntity } from './ticket-type.reducer';

export const TicketTypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const events = useAppSelector(state => state.gateway.event.entities);
  const ticketTypeEntity = useAppSelector(state => state.gateway.ticketType.entity);
  const loading = useAppSelector(state => state.gateway.ticketType.loading);
  const updating = useAppSelector(state => state.gateway.ticketType.updating);
  const updateSuccess = useAppSelector(state => state.gateway.ticketType.updateSuccess);
  const ticketTypeStatusValues = Object.keys(TicketTypeStatus);

  const handleClose = () => {
    navigate(`/ticket-type${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEvents({}));
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
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    if (values.totalQuantity !== undefined && typeof values.totalQuantity !== 'number') {
      values.totalQuantity = Number(values.totalQuantity);
    }
    if (values.availableQuantity !== undefined && typeof values.availableQuantity !== 'number') {
      values.availableQuantity = Number(values.availableQuantity);
    }
    if (values.reservedQuantity !== undefined && typeof values.reservedQuantity !== 'number') {
      values.reservedQuantity = Number(values.reservedQuantity);
    }
    if (values.soldQuantity !== undefined && typeof values.soldQuantity !== 'number') {
      values.soldQuantity = Number(values.soldQuantity);
    }
    if (values.maxPerOrder !== undefined && typeof values.maxPerOrder !== 'number') {
      values.maxPerOrder = Number(values.maxPerOrder);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...ticketTypeEntity,
      ...values,
      event: events.find(it => it.id.toString() === values.event?.toString()),
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
          updatedAt: displayDefaultDateTime(),
        }
      : {
          status: 'SELLING',
          ...ticketTypeEntity,
          createdAt: convertDateTimeFromServer(ticketTypeEntity.createdAt),
          updatedAt: convertDateTimeFromServer(ticketTypeEntity.updatedAt),
          event: ticketTypeEntity?.event?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.eventServiceTicketType.home.createOrEditLabel" data-cy="TicketTypeCreateUpdateHeading">
            <Translate contentKey="gatewayApp.eventServiceTicketType.home.createOrEditLabel">Create or edit a TicketType</Translate>
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
                  id="ticket-type-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketType.name')}
                id="ticket-type-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketType.description')}
                id="ticket-type-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketType.price')}
                id="ticket-type-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketType.currency')}
                id="ticket-type-currency"
                name="currency"
                data-cy="currency"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 10, message: translate('entity.validation.maxlength', { max: 10 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketType.totalQuantity')}
                id="ticket-type-totalQuantity"
                name="totalQuantity"
                data-cy="totalQuantity"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketType.availableQuantity')}
                id="ticket-type-availableQuantity"
                name="availableQuantity"
                data-cy="availableQuantity"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketType.reservedQuantity')}
                id="ticket-type-reservedQuantity"
                name="reservedQuantity"
                data-cy="reservedQuantity"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketType.soldQuantity')}
                id="ticket-type-soldQuantity"
                name="soldQuantity"
                data-cy="soldQuantity"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketType.maxPerOrder')}
                id="ticket-type-maxPerOrder"
                name="maxPerOrder"
                data-cy="maxPerOrder"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketType.status')}
                id="ticket-type-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {ticketTypeStatusValues.map(ticketTypeStatus => (
                  <option value={ticketTypeStatus} key={ticketTypeStatus}>
                    {translate(`gatewayApp.TicketTypeStatus.${ticketTypeStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketType.createdAt')}
                id="ticket-type-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.eventServiceTicketType.updatedAt')}
                id="ticket-type-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="ticket-type-event"
                name="event"
                data-cy="event"
                label={translate('gatewayApp.eventServiceTicketType.event')}
                type="select"
                required
              >
                <option value="" key="0" />
                {events
                  ? events.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ticket-type" replace color="info">
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

export default TicketTypeUpdate;
