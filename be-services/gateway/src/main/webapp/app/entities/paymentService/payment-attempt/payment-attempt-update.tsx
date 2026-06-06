import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPayments } from 'app/entities/paymentService/payment/payment.reducer';
import { PaymentProvider } from 'app/shared/model/enumerations/payment-provider.model';
import { PaymentAttemptStatus } from 'app/shared/model/enumerations/payment-attempt-status.model';
import { createEntity, getEntity, reset, updateEntity } from './payment-attempt.reducer';

export const PaymentAttemptUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const payments = useAppSelector(state => state.gateway.payment.entities);
  const paymentAttemptEntity = useAppSelector(state => state.gateway.paymentAttempt.entity);
  const loading = useAppSelector(state => state.gateway.paymentAttempt.loading);
  const updating = useAppSelector(state => state.gateway.paymentAttempt.updating);
  const updateSuccess = useAppSelector(state => state.gateway.paymentAttempt.updateSuccess);
  const paymentProviderValues = Object.keys(PaymentProvider);
  const paymentAttemptStatusValues = Object.keys(PaymentAttemptStatus);

  const handleClose = () => {
    navigate(`/payment-attempt${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPayments({}));
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
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...paymentAttemptEntity,
      ...values,
      payment: payments.find(it => it.id.toString() === values.payment?.toString()),
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
          updatedAt: displayDefaultDateTime(),
        }
      : {
          provider: 'STRIPE',
          status: 'PENDING',
          ...paymentAttemptEntity,
          createdAt: convertDateTimeFromServer(paymentAttemptEntity.createdAt),
          updatedAt: convertDateTimeFromServer(paymentAttemptEntity.updatedAt),
          payment: paymentAttemptEntity?.payment?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.paymentServicePaymentAttempt.home.createOrEditLabel" data-cy="PaymentAttemptCreateUpdateHeading">
            <Translate contentKey="gatewayApp.paymentServicePaymentAttempt.home.createOrEditLabel">
              Create or edit a PaymentAttempt
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
                  id="payment-attempt-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentAttempt.provider')}
                id="payment-attempt-provider"
                name="provider"
                data-cy="provider"
                type="select"
              >
                {paymentProviderValues.map(paymentProvider => (
                  <option value={paymentProvider} key={paymentProvider}>
                    {translate(`gatewayApp.PaymentProvider.${paymentProvider}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentAttempt.providerCheckoutSessionId')}
                id="payment-attempt-providerCheckoutSessionId"
                name="providerCheckoutSessionId"
                data-cy="providerCheckoutSessionId"
                type="text"
                validate={{
                  maxLength: { value: 512, message: translate('entity.validation.maxlength', { max: 512 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentAttempt.providerPaymentId')}
                id="payment-attempt-providerPaymentId"
                name="providerPaymentId"
                data-cy="providerPaymentId"
                type="text"
                validate={{
                  maxLength: { value: 512, message: translate('entity.validation.maxlength', { max: 512 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentAttempt.checkoutUrl')}
                id="payment-attempt-checkoutUrl"
                name="checkoutUrl"
                data-cy="checkoutUrl"
                type="text"
                validate={{
                  maxLength: { value: 2048, message: translate('entity.validation.maxlength', { max: 2048 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentAttempt.status')}
                id="payment-attempt-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {paymentAttemptStatusValues.map(paymentAttemptStatus => (
                  <option value={paymentAttemptStatus} key={paymentAttemptStatus}>
                    {translate(`gatewayApp.PaymentAttemptStatus.${paymentAttemptStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentAttempt.createdAt')}
                id="payment-attempt-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentServicePaymentAttempt.updatedAt')}
                id="payment-attempt-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="payment-attempt-payment"
                name="payment"
                data-cy="payment"
                label={translate('gatewayApp.paymentServicePaymentAttempt.payment')}
                type="select"
                required
              >
                <option value="" key="0" />
                {payments
                  ? payments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/payment-attempt" replace color="info">
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

export default PaymentAttemptUpdate;
