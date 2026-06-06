import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getEmailNotifications } from 'app/entities/notificationService/email-notification/email-notification.reducer';
import { createEntity, getEntity, reset, updateEntity } from './email-notification-item.reducer';

export const EmailNotificationItemUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const emailNotifications = useAppSelector(state => state.gateway.emailNotification.entities);
  const emailNotificationItemEntity = useAppSelector(state => state.gateway.emailNotificationItem.entity);
  const loading = useAppSelector(state => state.gateway.emailNotificationItem.loading);
  const updating = useAppSelector(state => state.gateway.emailNotificationItem.updating);
  const updateSuccess = useAppSelector(state => state.gateway.emailNotificationItem.updateSuccess);

  const handleClose = () => {
    navigate('/email-notification-item');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEmailNotifications({}));
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
    if (values.ticketId !== undefined && typeof values.ticketId !== 'number') {
      values.ticketId = Number(values.ticketId);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...emailNotificationItemEntity,
      ...values,
      notification: emailNotifications.find(it => it.id.toString() === values.notification?.toString()),
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
          ...emailNotificationItemEntity,
          createdAt: convertDateTimeFromServer(emailNotificationItemEntity.createdAt),
          notification: emailNotificationItemEntity?.notification?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="gatewayApp.notificationServiceEmailNotificationItem.home.createOrEditLabel"
            data-cy="EmailNotificationItemCreateUpdateHeading"
          >
            <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.home.createOrEditLabel">
              Create or edit a EmailNotificationItem
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
                  id="email-notification-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotificationItem.ticketId')}
                id="email-notification-item-ticketId"
                name="ticketId"
                data-cy="ticketId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotificationItem.ticketCode')}
                id="email-notification-item-ticketCode"
                name="ticketCode"
                data-cy="ticketCode"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationServiceEmailNotificationItem.createdAt')}
                id="email-notification-item-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="email-notification-item-notification"
                name="notification"
                data-cy="notification"
                label={translate('gatewayApp.notificationServiceEmailNotificationItem.notification')}
                type="select"
                required
              >
                <option value="" key="0" />
                {emailNotifications
                  ? emailNotifications.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/email-notification-item" replace color="info">
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

export default EmailNotificationItemUpdate;
