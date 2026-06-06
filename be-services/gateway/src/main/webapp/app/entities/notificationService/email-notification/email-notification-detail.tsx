import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './email-notification.reducer';

export const EmailNotificationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const emailNotificationEntity = useAppSelector(state => state.gateway.emailNotification.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="emailNotificationDetailsHeading">
          <Translate contentKey="gatewayApp.notificationServiceEmailNotification.detail.title">EmailNotification</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{emailNotificationEntity.id}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotification.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{emailNotificationEntity.userId}</dd>
          <dt>
            <span id="bookingId">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotification.bookingId">Booking Id</Translate>
            </span>
          </dt>
          <dd>{emailNotificationEntity.bookingId}</dd>
          <dt>
            <span id="recipientEmail">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotification.recipientEmail">Recipient Email</Translate>
            </span>
          </dt>
          <dd>{emailNotificationEntity.recipientEmail}</dd>
          <dt>
            <span id="subject">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotification.subject">Subject</Translate>
            </span>
          </dt>
          <dd>{emailNotificationEntity.subject}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotification.content">Content</Translate>
            </span>
          </dt>
          <dd>{emailNotificationEntity.content}</dd>
          <dt>
            <span id="provider">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotification.provider">Provider</Translate>
            </span>
          </dt>
          <dd>{emailNotificationEntity.provider}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotification.status">Status</Translate>
            </span>
          </dt>
          <dd>{emailNotificationEntity.status}</dd>
          <dt>
            <span id="providerMessageId">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotification.providerMessageId">Provider Message Id</Translate>
            </span>
          </dt>
          <dd>{emailNotificationEntity.providerMessageId}</dd>
          <dt>
            <span id="errorMessage">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotification.errorMessage">Error Message</Translate>
            </span>
          </dt>
          <dd>{emailNotificationEntity.errorMessage}</dd>
          <dt>
            <span id="sentAt">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotification.sentAt">Sent At</Translate>
            </span>
          </dt>
          <dd>
            {emailNotificationEntity.sentAt ? (
              <TextFormat value={emailNotificationEntity.sentAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotification.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {emailNotificationEntity.createdAt ? (
              <TextFormat value={emailNotificationEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotification.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {emailNotificationEntity.updatedAt ? (
              <TextFormat value={emailNotificationEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/email-notification" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/email-notification/${emailNotificationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmailNotificationDetail;
