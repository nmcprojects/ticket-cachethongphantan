import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { EventProcessStatus } from 'app/shared/model/enumerations/event-process-status.model';
import { createEntity, getEntity, reset, updateEntity } from './notification-inbox-event.reducer';

export const NotificationInboxEventUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const notificationInboxEventEntity = useAppSelector(state => state.gateway.notificationInboxEvent.entity);
  const loading = useAppSelector(state => state.gateway.notificationInboxEvent.loading);
  const updating = useAppSelector(state => state.gateway.notificationInboxEvent.updating);
  const updateSuccess = useAppSelector(state => state.gateway.notificationInboxEvent.updateSuccess);
  const eventProcessStatusValues = Object.keys(EventProcessStatus);

  const handleClose = () => {
    navigate('/notification-inbox-event');
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
    values.receivedAt = convertDateTimeToServer(values.receivedAt);
    values.processedAt = convertDateTimeToServer(values.processedAt);

    const entity = {
      ...notificationInboxEventEntity,
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
          receivedAt: displayDefaultDateTime(),
          processedAt: displayDefaultDateTime(),
        }
      : {
          status: 'RECEIVED',
          ...notificationInboxEventEntity,
          receivedAt: convertDateTimeFromServer(notificationInboxEventEntity.receivedAt),
          processedAt: convertDateTimeFromServer(notificationInboxEventEntity.processedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="gatewayApp.notificationServiceNotificationInboxEvent.home.createOrEditLabel"
            data-cy="NotificationInboxEventCreateUpdateHeading"
          >
            <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.home.createOrEditLabel">
              Create or edit a NotificationInboxEvent
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
                  id="notification-inbox-event-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.notificationServiceNotificationInboxEvent.sourceService')}
                id="notification-inbox-event-sourceService"
                name="sourceService"
                data-cy="sourceService"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceNotificationInboxEvent.eventId')}
                id="notification-inbox-event-eventId"
                name="eventId"
                data-cy="eventId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceNotificationInboxEvent.eventType')}
                id="notification-inbox-event-eventType"
                name="eventType"
                data-cy="eventType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceNotificationInboxEvent.payload')}
                id="notification-inbox-event-payload"
                name="payload"
                data-cy="payload"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceNotificationInboxEvent.status')}
                id="notification-inbox-event-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {eventProcessStatusValues.map(eventProcessStatus => (
                  <option value={eventProcessStatus} key={eventProcessStatus}>
                    {translate(`gatewayApp.EventProcessStatus.${eventProcessStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.notificationServiceNotificationInboxEvent.receivedAt')}
                id="notification-inbox-event-receivedAt"
                name="receivedAt"
                data-cy="receivedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceNotificationInboxEvent.processedAt')}
                id="notification-inbox-event-processedAt"
                name="processedAt"
                data-cy="processedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceNotificationInboxEvent.errorMessage')}
                id="notification-inbox-event-errorMessage"
                name="errorMessage"
                data-cy="errorMessage"
                type="text"
                validate={{
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/notification-inbox-event" replace color="info">
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

export default NotificationInboxEventUpdate;
