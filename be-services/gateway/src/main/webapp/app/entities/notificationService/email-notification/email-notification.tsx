import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './email-notification.reducer';

export const EmailNotification = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const emailNotificationList = useAppSelector(state => state.gateway.emailNotification.entities);
  const loading = useAppSelector(state => state.gateway.emailNotification.loading);
  const totalItems = useAppSelector(state => state.gateway.emailNotification.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="email-notification-heading" data-cy="EmailNotificationHeading">
        <Translate contentKey="gatewayApp.notificationServiceEmailNotification.home.title">Email Notifications</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.notificationServiceEmailNotification.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/email-notification/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.notificationServiceEmailNotification.home.createLabel">
              Create new Email Notification
            </Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {emailNotificationList && emailNotificationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotification.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('userId')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotification.userId">User Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userId')} />
                </th>
                <th className="hand" onClick={sort('bookingId')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotification.bookingId">Booking Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('bookingId')} />
                </th>
                <th className="hand" onClick={sort('recipientEmail')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotification.recipientEmail">Recipient Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('recipientEmail')} />
                </th>
                <th className="hand" onClick={sort('subject')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotification.subject">Subject</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subject')} />
                </th>
                <th className="hand" onClick={sort('content')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotification.content">Content</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('content')} />
                </th>
                <th className="hand" onClick={sort('provider')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotification.provider">Provider</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('provider')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotification.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('providerMessageId')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotification.providerMessageId">Provider Message Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('providerMessageId')} />
                </th>
                <th className="hand" onClick={sort('errorMessage')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotification.errorMessage">Error Message</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('errorMessage')} />
                </th>
                <th className="hand" onClick={sort('sentAt')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotification.sentAt">Sent At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sentAt')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotification.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="gatewayApp.notificationServiceEmailNotification.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {emailNotificationList.map((emailNotification, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/email-notification/${emailNotification.id}`} color="link" size="sm">
                      {emailNotification.id}
                    </Button>
                  </td>
                  <td>{emailNotification.userId}</td>
                  <td>{emailNotification.bookingId}</td>
                  <td>{emailNotification.recipientEmail}</td>
                  <td>{emailNotification.subject}</td>
                  <td>{emailNotification.content}</td>
                  <td>
                    <Translate contentKey={`gatewayApp.NotificationProvider.${emailNotification.provider}`} />
                  </td>
                  <td>
                    <Translate contentKey={`gatewayApp.NotificationStatus.${emailNotification.status}`} />
                  </td>
                  <td>{emailNotification.providerMessageId}</td>
                  <td>{emailNotification.errorMessage}</td>
                  <td>
                    {emailNotification.sentAt ? <TextFormat type="date" value={emailNotification.sentAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {emailNotification.createdAt ? (
                      <TextFormat type="date" value={emailNotification.createdAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {emailNotification.updatedAt ? (
                      <TextFormat type="date" value={emailNotification.updatedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/email-notification/${emailNotification.id}`}
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
                        to={`/email-notification/${emailNotification.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        onClick={() =>
                          (window.location.href = `/email-notification/${emailNotification.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
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
              <Translate contentKey="gatewayApp.notificationServiceEmailNotification.home.notFound">No Email Notifications found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={emailNotificationList && emailNotificationList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default EmailNotification;
