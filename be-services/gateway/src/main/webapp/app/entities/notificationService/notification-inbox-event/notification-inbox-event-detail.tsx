import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './notification-inbox-event.reducer';

export const NotificationInboxEventDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const notificationInboxEventEntity = useAppSelector(state => state.gateway.notificationInboxEvent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="notificationInboxEventDetailsHeading">
          <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.detail.title">NotificationInboxEvent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{notificationInboxEventEntity.id}</dd>
          <dt>
            <span id="sourceService">
              <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.sourceService">Source Service</Translate>
            </span>
          </dt>
          <dd>{notificationInboxEventEntity.sourceService}</dd>
          <dt>
            <span id="eventId">
              <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.eventId">Event Id</Translate>
            </span>
          </dt>
          <dd>{notificationInboxEventEntity.eventId}</dd>
          <dt>
            <span id="eventType">
              <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.eventType">Event Type</Translate>
            </span>
          </dt>
          <dd>{notificationInboxEventEntity.eventType}</dd>
          <dt>
            <span id="payload">
              <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.payload">Payload</Translate>
            </span>
          </dt>
          <dd>{notificationInboxEventEntity.payload}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.status">Status</Translate>
            </span>
          </dt>
          <dd>{notificationInboxEventEntity.status}</dd>
          <dt>
            <span id="receivedAt">
              <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.receivedAt">Received At</Translate>
            </span>
          </dt>
          <dd>
            {notificationInboxEventEntity.receivedAt ? (
              <TextFormat value={notificationInboxEventEntity.receivedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="processedAt">
              <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.processedAt">Processed At</Translate>
            </span>
          </dt>
          <dd>
            {notificationInboxEventEntity.processedAt ? (
              <TextFormat value={notificationInboxEventEntity.processedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="errorMessage">
              <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.errorMessage">Error Message</Translate>
            </span>
          </dt>
          <dd>{notificationInboxEventEntity.errorMessage}</dd>
        </dl>
        <Button tag={Link} to="/notification-inbox-event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/notification-inbox-event/${notificationInboxEventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NotificationInboxEventDetail;
