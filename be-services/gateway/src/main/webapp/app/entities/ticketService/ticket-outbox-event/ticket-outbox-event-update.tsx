import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { OutboxStatus } from 'app/shared/model/enumerations/outbox-status.model';
import { createEntity, getEntity, reset, updateEntity } from './ticket-outbox-event.reducer';

export const TicketOutboxEventUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ticketOutboxEventEntity = useAppSelector(state => state.gateway.ticketOutboxEvent.entity);
  const loading = useAppSelector(state => state.gateway.ticketOutboxEvent.loading);
  const updating = useAppSelector(state => state.gateway.ticketOutboxEvent.updating);
  const updateSuccess = useAppSelector(state => state.gateway.ticketOutboxEvent.updateSuccess);
  const outboxStatusValues = Object.keys(OutboxStatus);

  const handleClose = () => {
    navigate('/ticket-outbox-event');
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
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.publishedAt = convertDateTimeToServer(values.publishedAt);

    const entity = {
      ...ticketOutboxEventEntity,
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
          createdAt: displayDefaultDateTime(),
          publishedAt: displayDefaultDateTime(),
        }
      : {
          status: 'PENDING',
          ...ticketOutboxEventEntity,
          createdAt: convertDateTimeFromServer(ticketOutboxEventEntity.createdAt),
          publishedAt: convertDateTimeFromServer(ticketOutboxEventEntity.publishedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.ticketServiceTicketOutboxEvent.home.createOrEditLabel" data-cy="TicketOutboxEventCreateUpdateHeading">
            <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.home.createOrEditLabel">
              Create or edit a TicketOutboxEvent
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
                  id="ticket-outbox-event-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicketOutboxEvent.aggregateType')}
                id="ticket-outbox-event-aggregateType"
                name="aggregateType"
                data-cy="aggregateType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicketOutboxEvent.aggregateId')}
                id="ticket-outbox-event-aggregateId"
                name="aggregateId"
                data-cy="aggregateId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicketOutboxEvent.eventType')}
                id="ticket-outbox-event-eventType"
                name="eventType"
                data-cy="eventType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicketOutboxEvent.payload')}
                id="ticket-outbox-event-payload"
                name="payload"
                data-cy="payload"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicketOutboxEvent.status')}
                id="ticket-outbox-event-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {outboxStatusValues.map(outboxStatus => (
                  <option value={outboxStatus} key={outboxStatus}>
                    {translate(`gatewayApp.OutboxStatus.${outboxStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicketOutboxEvent.createdAt')}
                id="ticket-outbox-event-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicketOutboxEvent.publishedAt')}
                id="ticket-outbox-event-publishedAt"
                name="publishedAt"
                data-cy="publishedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceTicketOutboxEvent.errorMessage')}
                id="ticket-outbox-event-errorMessage"
                name="errorMessage"
                data-cy="errorMessage"
                type="text"
                validate={{
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ticket-outbox-event" replace color="info">
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

export default TicketOutboxEventUpdate;
