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

import { getEntities } from './ticket-outbox-event.reducer';

export const TicketOutboxEvent = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const ticketOutboxEventList = useAppSelector(state => state.gateway.ticketOutboxEvent.entities);
  const loading = useAppSelector(state => state.gateway.ticketOutboxEvent.loading);

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
      <h2 id="ticket-outbox-event-heading" data-cy="TicketOutboxEventHeading">
        <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.home.title">Ticket Outbox Events</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/ticket-outbox-event/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.home.createLabel">Create new Ticket Outbox Event</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ticketOutboxEventList && ticketOutboxEventList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('aggregateType')}>
                  <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.aggregateType">Aggregate Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('aggregateType')} />
                </th>
                <th className="hand" onClick={sort('aggregateId')}>
                  <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.aggregateId">Aggregate Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('aggregateId')} />
                </th>
                <th className="hand" onClick={sort('eventType')}>
                  <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.eventType">Event Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('eventType')} />
                </th>
                <th className="hand" onClick={sort('payload')}>
                  <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.payload">Payload</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('payload')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('publishedAt')}>
                  <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.publishedAt">Published At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('publishedAt')} />
                </th>
                <th className="hand" onClick={sort('errorMessage')}>
                  <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.errorMessage">Error Message</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('errorMessage')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ticketOutboxEventList.map((ticketOutboxEvent, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/ticket-outbox-event/${ticketOutboxEvent.id}`} color="link" size="sm">
                      {ticketOutboxEvent.id}
                    </Button>
                  </td>
                  <td>{ticketOutboxEvent.aggregateType}</td>
                  <td>{ticketOutboxEvent.aggregateId}</td>
                  <td>{ticketOutboxEvent.eventType}</td>
                  <td>{ticketOutboxEvent.payload}</td>
                  <td>
                    <Translate contentKey={`gatewayApp.OutboxStatus.${ticketOutboxEvent.status}`} />
                  </td>
                  <td>
                    {ticketOutboxEvent.createdAt ? (
                      <TextFormat type="date" value={ticketOutboxEvent.createdAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {ticketOutboxEvent.publishedAt ? (
                      <TextFormat type="date" value={ticketOutboxEvent.publishedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{ticketOutboxEvent.errorMessage}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/ticket-outbox-event/${ticketOutboxEvent.id}`}
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
                        to={`/ticket-outbox-event/${ticketOutboxEvent.id}/edit`}
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
                        onClick={() => (window.location.href = `/ticket-outbox-event/${ticketOutboxEvent.id}/delete`)}
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
              <Translate contentKey="gatewayApp.ticketServiceTicketOutboxEvent.home.notFound">No Ticket Outbox Events found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default TicketOutboxEvent;
