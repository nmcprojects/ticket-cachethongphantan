import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment.reducer';

export const PaymentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentEntity = useAppSelector(state => state.gateway.payment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentDetailsHeading">
          <Translate contentKey="gatewayApp.paymentServicePayment.detail.title">Payment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.id}</dd>
          <dt>
            <span id="bookingId">
              <Translate contentKey="gatewayApp.paymentServicePayment.bookingId">Booking Id</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.bookingId}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="gatewayApp.paymentServicePayment.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.userId}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="gatewayApp.paymentServicePayment.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.amount}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="gatewayApp.paymentServicePayment.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.currency}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.paymentServicePayment.status">Status</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.status}</dd>
          <dt>
            <span id="paidAt">
              <Translate contentKey="gatewayApp.paymentServicePayment.paidAt">Paid At</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.paidAt ? <TextFormat value={paymentEntity.paidAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="failedAt">
              <Translate contentKey="gatewayApp.paymentServicePayment.failedAt">Failed At</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.failedAt ? <TextFormat value={paymentEntity.failedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="expiredAt">
              <Translate contentKey="gatewayApp.paymentServicePayment.expiredAt">Expired At</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.expiredAt ? <TextFormat value={paymentEntity.expiredAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="gatewayApp.paymentServicePayment.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.createdAt ? <TextFormat value={paymentEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="gatewayApp.paymentServicePayment.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.updatedAt ? <TextFormat value={paymentEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/payment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment/${paymentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentDetail;
