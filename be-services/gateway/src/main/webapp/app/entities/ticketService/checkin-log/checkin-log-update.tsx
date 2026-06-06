import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTickets } from 'app/entities/ticketService/ticket/ticket.reducer';
import { CheckinResult } from 'app/shared/model/enumerations/checkin-result.model';
import { createEntity, getEntity, reset, updateEntity } from './checkin-log.reducer';

export const CheckinLogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const tickets = useAppSelector(state => state.gateway.ticket.entities);
  const checkinLogEntity = useAppSelector(state => state.gateway.checkinLog.entity);
  const loading = useAppSelector(state => state.gateway.checkinLog.loading);
  const updating = useAppSelector(state => state.gateway.checkinLog.updating);
  const updateSuccess = useAppSelector(state => state.gateway.checkinLog.updateSuccess);
  const checkinResultValues = Object.keys(CheckinResult);

  const handleClose = () => {
    navigate('/checkin-log');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTickets({}));
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
    if (values.staffId !== undefined && typeof values.staffId !== 'number') {
      values.staffId = Number(values.staffId);
    }
    if (values.eventId !== undefined && typeof values.eventId !== 'number') {
      values.eventId = Number(values.eventId);
    }
    values.checkedInAt = convertDateTimeToServer(values.checkedInAt);

    const entity = {
      ...checkinLogEntity,
      ...values,
      ticket: tickets.find(it => it.id.toString() === values.ticket?.toString()),
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
          checkedInAt: displayDefaultDateTime(),
        }
      : {
          result: 'VALID',
          ...checkinLogEntity,
          checkedInAt: convertDateTimeFromServer(checkinLogEntity.checkedInAt),
          ticket: checkinLogEntity?.ticket?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.ticketServiceCheckinLog.home.createOrEditLabel" data-cy="CheckinLogCreateUpdateHeading">
            <Translate contentKey="gatewayApp.ticketServiceCheckinLog.home.createOrEditLabel">Create or edit a CheckinLog</Translate>
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
                  id="checkin-log-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.ticketServiceCheckinLog.ticketCode')}
                id="checkin-log-ticketCode"
                name="ticketCode"
                data-cy="ticketCode"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceCheckinLog.staffId')}
                id="checkin-log-staffId"
                name="staffId"
                data-cy="staffId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceCheckinLog.eventId')}
                id="checkin-log-eventId"
                name="eventId"
                data-cy="eventId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceCheckinLog.result')}
                id="checkin-log-result"
                name="result"
                data-cy="result"
                type="select"
              >
                {checkinResultValues.map(checkinResult => (
                  <option value={checkinResult} key={checkinResult}>
                    {translate(`gatewayApp.CheckinResult.${checkinResult}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.ticketServiceCheckinLog.message')}
                id="checkin-log-message"
                name="message"
                data-cy="message"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.ticketServiceCheckinLog.checkedInAt')}
                id="checkin-log-checkedInAt"
                name="checkedInAt"
                data-cy="checkedInAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="checkin-log-ticket"
                name="ticket"
                data-cy="ticket"
                label={translate('gatewayApp.ticketServiceCheckinLog.ticket')}
                type="select"
              >
                <option value="" key="0" />
                {tickets
                  ? tickets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/checkin-log" replace color="info">
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

export default CheckinLogUpdate;
