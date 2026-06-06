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

import { getEntities } from './booking-outbox-event.reducer';

export const BookingOutboxEvent = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const bookingOutboxEventList = useAppSelector(state => state.gateway.bookingOutboxEvent.entities);
  const loading = useAppSelector(state => state.gateway.bookingOutboxEvent.loading);

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
      <h2 id="booking-outbox-event-heading" data-cy="BookingOutboxEventHeading">
        <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.home.title">Booking Outbox Events</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/booking-outbox-event/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.home.createLabel">Create new Booking Outbox Event</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {bookingOutboxEventList && bookingOutboxEventList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('aggregateType')}>
                  <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.aggregateType">Aggregate Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('aggregateType')} />
                </th>
                <th className="hand" onClick={sort('aggregateId')}>
                  <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.aggregateId">Aggregate Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('aggregateId')} />
                </th>
                <th className="hand" onClick={sort('eventType')}>
                  <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.eventType">Event Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('eventType')} />
                </th>
                <th className="hand" onClick={sort('payload')}>
                  <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.payload">Payload</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('payload')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('publishedAt')}>
                  <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.publishedAt">Published At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('publishedAt')} />
                </th>
                <th className="hand" onClick={sort('errorMessage')}>
                  <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.errorMessage">Error Message</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('errorMessage')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bookingOutboxEventList.map((bookingOutboxEvent, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/booking-outbox-event/${bookingOutboxEvent.id}`} color="link" size="sm">
                      {bookingOutboxEvent.id}
                    </Button>
                  </td>
                  <td>{bookingOutboxEvent.aggregateType}</td>
                  <td>{bookingOutboxEvent.aggregateId}</td>
                  <td>{bookingOutboxEvent.eventType}</td>
                  <td>{bookingOutboxEvent.payload}</td>
                  <td>
                    <Translate contentKey={`gatewayApp.OutboxStatus.${bookingOutboxEvent.status}`} />
                  </td>
                  <td>
                    {bookingOutboxEvent.createdAt ? (
                      <TextFormat type="date" value={bookingOutboxEvent.createdAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {bookingOutboxEvent.publishedAt ? (
                      <TextFormat type="date" value={bookingOutboxEvent.publishedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{bookingOutboxEvent.errorMessage}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/booking-outbox-event/${bookingOutboxEvent.id}`}
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
                        to={`/booking-outbox-event/${bookingOutboxEvent.id}/edit`}
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
                        onClick={() => (window.location.href = `/booking-outbox-event/${bookingOutboxEvent.id}/delete`)}
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
              <Translate contentKey="gatewayApp.bookingServiceBookingOutboxEvent.home.notFound">No Booking Outbox Events found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default BookingOutboxEvent;
