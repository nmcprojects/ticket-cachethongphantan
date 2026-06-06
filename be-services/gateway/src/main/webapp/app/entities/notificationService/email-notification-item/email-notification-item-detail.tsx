import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './email-notification-item.reducer';

export const EmailNotificationItemDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const emailNotificationItemEntity = useAppSelector(state => state.gateway.emailNotificationItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="emailNotificationItemDetailsHeading">
          <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.detail.title">EmailNotificationItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{emailNotificationItemEntity.id}</dd>
          <dt>
            <span id="ticketId">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.ticketId">Ticket Id</Translate>
            </span>
          </dt>
          <dd>{emailNotificationItemEntity.ticketId}</dd>
          <dt>
            <span id="ticketCode">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.ticketCode">Ticket Code</Translate>
            </span>
          </dt>
          <dd>{emailNotificationItemEntity.ticketCode}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {emailNotificationItemEntity.createdAt ? (
              <TextFormat value={emailNotificationItemEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.notification">Notification</Translate>
          </dt>
          <dd>{emailNotificationItemEntity.notification ? emailNotificationItemEntity.notification.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/email-notification-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/email-notification-item/${emailNotificationItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmailNotificationItemDetail;
