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

import { getEntities } from './checkin-log.reducer';

export const CheckinLog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const checkinLogList = useAppSelector(state => state.gateway.checkinLog.entities);
  const loading = useAppSelector(state => state.gateway.checkinLog.loading);

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
      <h2 id="checkin-log-heading" data-cy="CheckinLogHeading">
        <Translate contentKey="gatewayApp.ticketServiceCheckinLog.home.title">Checkin Logs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.ticketServiceCheckinLog.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/checkin-log/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.ticketServiceCheckinLog.home.createLabel">Create new Checkin Log</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {checkinLogList && checkinLogList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.ticketServiceCheckinLog.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('ticketCode')}>
                  <Translate contentKey="gatewayApp.ticketServiceCheckinLog.ticketCode">Ticket Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ticketCode')} />
                </th>
                <th className="hand" onClick={sort('staffId')}>
                  <Translate contentKey="gatewayApp.ticketServiceCheckinLog.staffId">Staff Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('staffId')} />
                </th>
                <th className="hand" onClick={sort('eventId')}>
                  <Translate contentKey="gatewayApp.ticketServiceCheckinLog.eventId">Event Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('eventId')} />
                </th>
                <th className="hand" onClick={sort('result')}>
                  <Translate contentKey="gatewayApp.ticketServiceCheckinLog.result">Result</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('result')} />
                </th>
                <th className="hand" onClick={sort('message')}>
                  <Translate contentKey="gatewayApp.ticketServiceCheckinLog.message">Message</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('message')} />
                </th>
                <th className="hand" onClick={sort('checkedInAt')}>
                  <Translate contentKey="gatewayApp.ticketServiceCheckinLog.checkedInAt">Checked In At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('checkedInAt')} />
                </th>
                <th>
                  <Translate contentKey="gatewayApp.ticketServiceCheckinLog.ticket">Ticket</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {checkinLogList.map((checkinLog, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/checkin-log/${checkinLog.id}`} color="link" size="sm">
                      {checkinLog.id}
                    </Button>
                  </td>
                  <td>{checkinLog.ticketCode}</td>
                  <td>{checkinLog.staffId}</td>
                  <td>{checkinLog.eventId}</td>
                  <td>
                    <Translate contentKey={`gatewayApp.CheckinResult.${checkinLog.result}`} />
                  </td>
                  <td>{checkinLog.message}</td>
                  <td>
                    {checkinLog.checkedInAt ? <TextFormat type="date" value={checkinLog.checkedInAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{checkinLog.ticket ? <Link to={`/ticket/${checkinLog.ticket.id}`}>{checkinLog.ticket.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/checkin-log/${checkinLog.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/checkin-log/${checkinLog.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/checkin-log/${checkinLog.id}/delete`)}
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
              <Translate contentKey="gatewayApp.ticketServiceCheckinLog.home.notFound">No Checkin Logs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CheckinLog;
