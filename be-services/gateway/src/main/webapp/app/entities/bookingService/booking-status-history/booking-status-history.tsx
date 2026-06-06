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

import { getEntities } from './booking-status-history.reducer';

export const BookingStatusHistory = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const bookingStatusHistoryList = useAppSelector(state => state.gateway.bookingStatusHistory.entities);
  const loading = useAppSelector(state => state.gateway.bookingStatusHistory.loading);

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
      <h2 id="booking-status-history-heading" data-cy="BookingStatusHistoryHeading">
        <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.home.title">Booking Status Histories</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/booking-status-history/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.home.createLabel">
              Create new Booking Status History
            </Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {bookingStatusHistoryList && bookingStatusHistoryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('oldStatus')}>
                  <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.oldStatus">Old Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('oldStatus')} />
                </th>
                <th className="hand" onClick={sort('newStatus')}>
                  <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.newStatus">New Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('newStatus')} />
                </th>
                <th className="hand" onClick={sort('reason')}>
                  <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.reason">Reason</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reason')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th>
                  <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.booking">Booking</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bookingStatusHistoryList.map((bookingStatusHistory, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/booking-status-history/${bookingStatusHistory.id}`} color="link" size="sm">
                      {bookingStatusHistory.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`gatewayApp.BookingStatus.${bookingStatusHistory.oldStatus}`} />
                  </td>
                  <td>
                    <Translate contentKey={`gatewayApp.BookingStatus.${bookingStatusHistory.newStatus}`} />
                  </td>
                  <td>{bookingStatusHistory.reason}</td>
                  <td>
                    {bookingStatusHistory.createdAt ? (
                      <TextFormat type="date" value={bookingStatusHistory.createdAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {bookingStatusHistory.booking ? (
                      <Link to={`/booking/${bookingStatusHistory.booking.id}`}>{bookingStatusHistory.booking.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/booking-status-history/${bookingStatusHistory.id}`}
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
                        to={`/booking-status-history/${bookingStatusHistory.id}/edit`}
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
                        onClick={() => (window.location.href = `/booking-status-history/${bookingStatusHistory.id}/delete`)}
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
              <Translate contentKey="gatewayApp.bookingServiceBookingStatusHistory.home.notFound">
                No Booking Status Histories found
              </Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default BookingStatusHistory;
