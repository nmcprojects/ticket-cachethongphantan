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

import { getEntities } from './email-notification-item.reducer';

export const EmailNotificationItem = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const emailNotificationItemList = useAppSelector(state => state.gateway.emailNotificationItem.entities);
  const loading = useAppSelector(state => state.gateway.emailNotificationItem.loading);

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
      <h2 id="email-notification-item-heading" data-cy="EmailNotificationItemHeading">
        <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.home.title">Email Notification Items</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/email-notification-item/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.home.createLabel">
              Create new Email Notification Item
            </Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {emailNotificationItemList && emailNotificationItemList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('ticketId')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.ticketId">Ticket Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ticketId')} />
                </th>
                <th className="hand" onClick={sort('ticketCode')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.ticketCode">Ticket Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ticketCode')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.notification">Notification</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {emailNotificationItemList.map((emailNotificationItem, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/email-notification-item/${emailNotificationItem.id}`} color="link" size="sm">
                      {emailNotificationItem.id}
                    </Button>
                  </td>
                  <td>{emailNotificationItem.ticketId}</td>
                  <td>{emailNotificationItem.ticketCode}</td>
                  <td>
                    {emailNotificationItem.createdAt ? (
                      <TextFormat type="date" value={emailNotificationItem.createdAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {emailNotificationItem.notification ? (
                      <Link to={`/email-notification/${emailNotificationItem.notification.id}`}>
                        {emailNotificationItem.notification.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/email-notification-item/${emailNotificationItem.id}`}
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
                        to={`/email-notification-item/${emailNotificationItem.id}/edit`}
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
                        onClick={() => (window.location.href = `/email-notification-item/${emailNotificationItem.id}/delete`)}
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
              <Translate contentKey="gatewayApp.notificationServiceEmailNotificationItem.home.notFound">
                No Email Notification Items found
              </Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default EmailNotificationItem;
