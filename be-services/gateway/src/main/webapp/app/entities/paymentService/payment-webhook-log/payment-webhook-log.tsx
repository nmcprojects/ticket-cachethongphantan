import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './payment-webhook-log.reducer';

export const PaymentWebhookLog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const paymentWebhookLogList = useAppSelector(state => state.gateway.paymentWebhookLog.entities);
  const loading = useAppSelector(state => state.gateway.paymentWebhookLog.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="payment-webhook-log-heading" data-cy="PaymentWebhookLogHeading">
        <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.home.title">Payment Webhook Logs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/payment-webhook-log/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.home.createLabel">Create new Payment Webhook Log</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {paymentWebhookLogList && paymentWebhookLogList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('provider')}>
                  <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.provider">Provider</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('provider')} />
                </th>
                <th className="hand" onClick={sort('providerEventId')}>
                  <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.providerEventId">Provider Event Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('providerEventId')} />
                </th>
                <th className="hand" onClick={sort('eventType')}>
                  <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.eventType">Event Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('eventType')} />
                </th>
                <th className="hand" onClick={sort('rawPayload')}>
                  <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.rawPayload">Raw Payload</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('rawPayload')} />
                </th>
                <th className="hand" onClick={sort('signature')}>
                  <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.signature">Signature</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('signature')} />
                </th>
                <th className="hand" onClick={sort('processed')}>
                  <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.processed">Processed</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('processed')} />
                </th>
                <th className="hand" onClick={sort('processingError')}>
                  <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.processingError">Processing Error</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('processingError')} />
                </th>
                <th className="hand" onClick={sort('receivedAt')}>
                  <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.receivedAt">Received At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('receivedAt')} />
                </th>
                <th className="hand" onClick={sort('processedAt')}>
                  <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.processedAt">Processed At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('processedAt')} />
                </th>
                <th>
                  <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.payment">Payment</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.attempt">Attempt</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {paymentWebhookLogList.map((paymentWebhookLog, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/payment-webhook-log/${paymentWebhookLog.id}`} color="link" size="sm">
                      {paymentWebhookLog.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`gatewayApp.PaymentProvider.${paymentWebhookLog.provider}`} />
                  </td>
                  <td>{paymentWebhookLog.providerEventId}</td>
                  <td>{paymentWebhookLog.eventType}</td>
                  <td>{paymentWebhookLog.rawPayload}</td>
                  <td>{paymentWebhookLog.signature}</td>
                  <td>{paymentWebhookLog.processed ? 'true' : 'false'}</td>
                  <td>{paymentWebhookLog.processingError}</td>
                  <td>
                    {paymentWebhookLog.receivedAt ? (
                      <TextFormat type="date" value={paymentWebhookLog.receivedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {paymentWebhookLog.processedAt ? (
                      <TextFormat type="date" value={paymentWebhookLog.processedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {paymentWebhookLog.payment ? (
                      <Link to={`/payment/${paymentWebhookLog.payment.id}`}>{paymentWebhookLog.payment.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {paymentWebhookLog.attempt ? (
                      <Link to={`/payment-attempt/${paymentWebhookLog.attempt.id}`}>{paymentWebhookLog.attempt.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/payment-webhook-log/${paymentWebhookLog.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/payment-webhook-log/${paymentWebhookLog.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/payment-webhook-log/${paymentWebhookLog.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="gatewayApp.paymentServicePaymentWebhookLog.home.notFound">No Payment Webhook Logs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PaymentWebhookLog;
