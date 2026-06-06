import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './organizer-profile.reducer';

export const OrganizerProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const organizerProfileEntity = useAppSelector(state => state.gateway.organizerProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="organizerProfileDetailsHeading">
          <Translate contentKey="gatewayApp.eventServiceOrganizerProfile.detail.title">OrganizerProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{organizerProfileEntity.id}</dd>
          <dt>
            <span id="authUserId">
              <Translate contentKey="gatewayApp.eventServiceOrganizerProfile.authUserId">Auth User Id</Translate>
            </span>
          </dt>
          <dd>{organizerProfileEntity.authUserId}</dd>
          <dt>
            <span id="organizationName">
              <Translate contentKey="gatewayApp.eventServiceOrganizerProfile.organizationName">Organization Name</Translate>
            </span>
          </dt>
          <dd>{organizerProfileEntity.organizationName}</dd>
          <dt>
            <span id="contactEmail">
              <Translate contentKey="gatewayApp.eventServiceOrganizerProfile.contactEmail">Contact Email</Translate>
            </span>
          </dt>
          <dd>{organizerProfileEntity.contactEmail}</dd>
          <dt>
            <span id="contactPhone">
              <Translate contentKey="gatewayApp.eventServiceOrganizerProfile.contactPhone">Contact Phone</Translate>
            </span>
          </dt>
          <dd>{organizerProfileEntity.contactPhone}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.eventServiceOrganizerProfile.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {organizerProfileEntity.createdAt ? (
              <TextFormat value={organizerProfileEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="gatewayApp.eventServiceOrganizerProfile.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {organizerProfileEntity.updatedAt ? (
              <TextFormat value={organizerProfileEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/organizer-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/organizer-profile/${organizerProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrganizerProfileDetail;
