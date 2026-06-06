import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CheckinLog from './checkin-log';
import CheckinLogDetail from './checkin-log-detail';
import CheckinLogUpdate from './checkin-log-update';
import CheckinLogDeleteDialog from './checkin-log-delete-dialog';

const CheckinLogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CheckinLog />} />
    <Route path="new" element={<CheckinLogUpdate />} />
    <Route path=":id">
      <Route index element={<CheckinLogDetail />} />
      <Route path="edit" element={<CheckinLogUpdate />} />
      <Route path="delete" element={<CheckinLogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CheckinLogRoutes;
