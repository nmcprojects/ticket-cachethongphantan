import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BookingStatusHistory from './booking-status-history';
import BookingStatusHistoryDetail from './booking-status-history-detail';
import BookingStatusHistoryUpdate from './booking-status-history-update';
import BookingStatusHistoryDeleteDialog from './booking-status-history-delete-dialog';

const BookingStatusHistoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BookingStatusHistory />} />
    <Route path="new" element={<BookingStatusHistoryUpdate />} />
    <Route path=":id">
      <Route index element={<BookingStatusHistoryDetail />} />
      <Route path="edit" element={<BookingStatusHistoryUpdate />} />
      <Route path="delete" element={<BookingStatusHistoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BookingStatusHistoryRoutes;
