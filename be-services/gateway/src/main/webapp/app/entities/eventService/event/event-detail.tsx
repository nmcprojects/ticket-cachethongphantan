import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './event.reducer';

export const EventDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const eventEntity = useAppSelector(state => state.gateway.event.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="eventDetailsHeading">
          <Translate contentKey="gatewayApp.eventServiceEvent.detail.title">Event</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{eventEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="gatewayApp.eventServiceEvent.title">Title</Translate>
            </span>
          </dt>
          <dd>{eventEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="gatewayApp.eventServiceEvent.description">Description</Translate>
            </span>
          </dt>
          <dd>{eventEntity.description}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="gatewayApp.eventServiceEvent.location">Location</Translate>
            </span>
          </dt>
          <dd>{eventEntity.location}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="gatewayApp.eventServiceEvent.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>{eventEntity.startTime ? <TextFormat value={eventEntity.startTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="gatewayApp.eventServiceEvent.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>{eventEntity.endTime ? <TextFormat value={eventEntity.endTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="bannerUrl">
              <Translate contentKey="gatewayApp.eventServiceEvent.bannerUrl">Banner Url</Translate>
            </span>
          </dt>
          <dd>{eventEntity.bannerUrl}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.eventServiceEvent.status">Status</Translate>
            </span>
          </dt>
          <dd>{eventEntity.status}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.eventServiceEvent.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{eventEntity.createdAt ? <TextFormat value={eventEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="gatewayApp.eventServiceEvent.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{eventEntity.updatedAt ? <TextFormat value={eventEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="gatewayApp.eventServiceEvent.organizer">Organizer</Translate>
          </dt>
          <dd>{eventEntity.organizer ? eventEntity.organizer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/event/${eventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EventDetail;
