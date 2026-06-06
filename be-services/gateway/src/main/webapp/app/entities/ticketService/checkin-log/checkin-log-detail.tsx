import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './checkin-log.reducer';

export const CheckinLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const checkinLogEntity = useAppSelector(state => state.gateway.checkinLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="checkinLogDetailsHeading">
          <Translate contentKey="gatewayApp.ticketServiceCheckinLog.detail.title">CheckinLog</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{checkinLogEntity.id}</dd>
          <dt>
            <span id="ticketCode">
              <Translate contentKey="gatewayApp.ticketServiceCheckinLog.ticketCode">Ticket Code</Translate>
            </span>
          </dt>
          <dd>{checkinLogEntity.ticketCode}</dd>
          <dt>
            <span id="staffId">
              <Translate contentKey="gatewayApp.ticketServiceCheckinLog.staffId">Staff Id</Translate>
            </span>
          </dt>
          <dd>{checkinLogEntity.staffId}</dd>
          <dt>
            <span id="eventId">
              <Translate contentKey="gatewayApp.ticketServiceCheckinLog.eventId">Event Id</Translate>
            </span>
          </dt>
          <dd>{checkinLogEntity.eventId}</dd>
          <dt>
            <span id="result">
              <Translate contentKey="gatewayApp.ticketServiceCheckinLog.result">Result</Translate>
            </span>
          </dt>
          <dd>{checkinLogEntity.result}</dd>
          <dt>
            <span id="message">
              <Translate contentKey="gatewayApp.ticketServiceCheckinLog.message">Message</Translate>
            </span>
          </dt>
          <dd>{checkinLogEntity.message}</dd>
          <dt>
            <span id="checkedInAt">
              <Translate contentKey="gatewayApp.ticketServiceCheckinLog.checkedInAt">Checked In At</Translate>
            </span>
          </dt>
          <dd>
            {checkinLogEntity.checkedInAt ? <TextFormat value={checkinLogEntity.checkedInAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="gatewayApp.ticketServiceCheckinLog.ticket">Ticket</Translate>
          </dt>
          <dd>{checkinLogEntity.ticket ? checkinLogEntity.ticket.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/checkin-log" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/checkin-log/${checkinLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CheckinLogDetail;
