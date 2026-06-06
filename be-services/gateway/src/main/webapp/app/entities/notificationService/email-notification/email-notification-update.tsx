import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { NotificationProvider } from 'app/shared/model/enumerations/notification-provider.model';
import { NotificationStatus } from 'app/shared/model/enumerations/notification-status.model';
import { createEntity, getEntity, reset, updateEntity } from './email-notification.reducer';

export const EmailNotificationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const emailNotificationEntity = useAppSelector(state => state.gateway.emailNotification.entity);
  const loading = useAppSelector(state => state.gateway.emailNotification.loading);
  const updating = useAppSelector(state => state.gateway.emailNotification.updating);
  const updateSuccess = useAppSelector(state => state.gateway.emailNotification.updateSuccess);
  const notificationProviderValues = Object.keys(NotificationProvider);
  const notificationStatusValues = Object.keys(NotificationStatus);

  const handleClose = () => {
    navigate(`/email-notification${location.search}`);
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
    if (values.userId !== undefined && typeof values.userId !== 'number') {
      values.userId = Number(values.userId);
    }
    if (values.bookingId !== undefined && typeof values.bookingId !== 'number') {
      values.bookingId = Number(values.bookingId);
    }
    values.sentAt = convertDateTimeToServer(values.sentAt);
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...emailNotificationEntity,
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
          sentAt: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          provider: 'SENDGRID',
          status: 'PENDING',
          ...emailNotificationEntity,
          sentAt: convertDateTimeFromServer(emailNotificationEntity.sentAt),
          createdAt: convertDateTimeFromServer(emailNotificationEntity.createdAt),
          updatedAt: convertDateTimeFromServer(emailNotificationEntity.updatedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.notificationServiceEmailNotification.home.createOrEditLabel" data-cy="EmailNotificationCreateUpdateHeading">
            <Translate contentKey="gatewayApp.notificationServiceEmailNotification.home.createOrEditLabel">
              Create or edit a EmailNotification
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
                  id="email-notification-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotification.userId')}
                id="email-notification-userId"
                name="userId"
                data-cy="userId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotification.bookingId')}
                id="email-notification-bookingId"
                name="bookingId"
                data-cy="bookingId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotification.recipientEmail')}
                id="email-notification-recipientEmail"
                name="recipientEmail"
                data-cy="recipientEmail"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotification.subject')}
                id="email-notification-subject"
                name="subject"
                data-cy="subject"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotification.content')}
                id="email-notification-content"
                name="content"
                data-cy="content"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotification.provider')}
                id="email-notification-provider"
                name="provider"
                data-cy="provider"
                type="select"
              >
                {notificationProviderValues.map(notificationProvider => (
                  <option value={notificationProvider} key={notificationProvider}>
                    {translate(`gatewayApp.NotificationProvider.${notificationProvider}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotification.status')}
                id="email-notification-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {notificationStatusValues.map(notificationStatus => (
                  <option value={notificationStatus} key={notificationStatus}>
                    {translate(`gatewayApp.NotificationStatus.${notificationStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotification.providerMessageId')}
                id="email-notification-providerMessageId"
                name="providerMessageId"
                data-cy="providerMessageId"
                type="text"
                validate={{
                  maxLength: { value: 512, message: translate('entity.validation.maxlength', { max: 512 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotification.errorMessage')}
                id="email-notification-errorMessage"
                name="errorMessage"
                data-cy="errorMessage"
                type="text"
                validate={{
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotification.sentAt')}
                id="email-notification-sentAt"
                name="sentAt"
                data-cy="sentAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotification.createdAt')}
                id="email-notification-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotification.updatedAt')}
                id="email-notification-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/email-notification" replace color="info">
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

export default EmailNotificationUpdate;
