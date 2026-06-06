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

import { getEntities } from './notification-inbox-event.reducer';

export const NotificationInboxEvent = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const notificationInboxEventList = useAppSelector(state => state.gateway.notificationInboxEvent.entities);
  const loading = useAppSelector(state => state.gateway.notificationInboxEvent.loading);

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
      <h2 id="notification-inbox-event-heading" data-cy="NotificationInboxEventHeading">
        <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.home.title">Notification Inbox Events</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/notification-inbox-event/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.home.createLabel">
              Create new Notification Inbox Event
            </Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {notificationInboxEventList && notificationInboxEventList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('sourceService')}>
                  <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.sourceService">Source Service</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sourceService')} />
                </th>
                <th className="hand" onClick={sort('eventId')}>
                  <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.eventId">Event Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('eventId')} />
                </th>
                <th className="hand" onClick={sort('eventType')}>
                  <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.eventType">Event Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('eventType')} />
                </th>
                <th className="hand" onClick={sort('payload')}>
                  <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.payload">Payload</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('payload')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('receivedAt')}>
                  <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.receivedAt">Received At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('receivedAt')} />
                </th>
                <th className="hand" onClick={sort('processedAt')}>
                  <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.processedAt">Processed At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('processedAt')} />
                </th>
                <th className="hand" onClick={sort('errorMessage')}>
                  <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.errorMessage">Error Message</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('errorMessage')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {notificationInboxEventList.map((notificationInboxEvent, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/notification-inbox-event/${notificationInboxEvent.id}`} color="link" size="sm">
                      {notificationInboxEvent.id}
                    </Button>
                  </td>
                  <td>{notificationInboxEvent.sourceService}</td>
                  <td>{notificationInboxEvent.eventId}</td>
                  <td>{notificationInboxEvent.eventType}</td>
                  <td>{notificationInboxEvent.payload}</td>
                  <td>
                    <Translate contentKey={`gatewayApp.EventProcessStatus.${notificationInboxEvent.status}`} />
                  </td>
                  <td>
                    {notificationInboxEvent.receivedAt ? (
                      <TextFormat type="date" value={notificationInboxEvent.receivedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {notificationInboxEvent.processedAt ? (
                      <TextFormat type="date" value={notificationInboxEvent.processedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{notificationInboxEvent.errorMessage}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/notification-inbox-event/${notificationInboxEvent.id}`}
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
                        to={`/notification-inbox-event/${notificationInboxEvent.id}/edit`}
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
                        onClick={() => (window.location.href = `/notification-inbox-event/${notificationInboxEvent.id}/delete`)}
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
              <Translate contentKey="gatewayApp.notificationServiceNotificationInboxEvent.home.notFound">
                No Notification Inbox Events found
              </Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default NotificationInboxEvent;
