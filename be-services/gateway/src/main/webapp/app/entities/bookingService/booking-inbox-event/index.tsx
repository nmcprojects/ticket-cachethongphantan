import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BookingInboxEvent from './booking-inbox-event';
import BookingInboxEventDetail from './booking-inbox-event-detail';
import BookingInboxEventUpdate from './booking-inbox-event-update';
import BookingInboxEventDeleteDialog from './booking-inbox-event-delete-dialog';

const BookingInboxEventRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BookingInboxEvent />} />
    <Route path="new" element={<BookingInboxEventUpdate />} />
    <Route path=":id">
      <Route index element={<BookingInboxEventDetail />} />
      <Route path="edit" element={<BookingInboxEventUpdate />} />
      <Route path="delete" element={<BookingInboxEventDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BookingInboxEventRoutes;
