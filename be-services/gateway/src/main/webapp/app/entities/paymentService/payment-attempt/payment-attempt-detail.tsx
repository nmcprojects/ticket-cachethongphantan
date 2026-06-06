import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment-attempt.reducer';

export const PaymentAttemptDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentAttemptEntity = useAppSelector(state => state.gateway.paymentAttempt.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentAttemptDetailsHeading">
          <Translate contentKey="gatewayApp.paymentServicePaymentAttempt.detail.title">PaymentAttempt</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentAttemptEntity.id}</dd>
          <dt>
            <span id="provider">
              <Translate contentKey="gatewayApp.paymentServicePaymentAttempt.provider">Provider</Translate>
            </span>
          </dt>
          <dd>{paymentAttemptEntity.provider}</dd>
          <dt>
            <span id="providerCheckoutSessionId">
              <Translate contentKey="gatewayApp.paymentServicePaymentAttempt.providerCheckoutSessionId">
                Provider Checkout Session Id
              </Translate>
            </span>
          </dt>
          <dd>{paymentAttemptEntity.providerCheckoutSessionId}</dd>
          <dt>
            <span id="providerPaymentId">
              <Translate contentKey="gatewayApp.paymentServicePaymentAttempt.providerPaymentId">Provider Payment Id</Translate>
            </span>
          </dt>
          <dd>{paymentAttemptEntity.providerPaymentId}</dd>
          <dt>
            <span id="checkoutUrl">
              <Translate contentKey="gatewayApp.paymentServicePaymentAttempt.checkoutUrl">Checkout Url</Translate>
            </span>
          </dt>
          <dd>{paymentAttemptEntity.checkoutUrl}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.paymentServicePaymentAttempt.status">Status</Translate>
            </span>
          </dt>
          <dd>{paymentAttemptEntity.status}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.paymentServicePaymentAttempt.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {paymentAttemptEntity.createdAt ? (
              <TextFormat value={paymentAttemptEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="gatewayApp.paymentServicePaymentAttempt.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {paymentAttemptEntity.updatedAt ? (
              <TextFormat value={paymentAttemptEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="gatewayApp.paymentServicePaymentAttempt.payment">Payment</Translate>
          </dt>
          <dd>{paymentAttemptEntity.payment ? paymentAttemptEntity.payment.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/payment-attempt" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment-attempt/${paymentAttemptEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentAttemptDetail;
