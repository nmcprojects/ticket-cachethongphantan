import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BookingOutboxEvent from './booking-outbox-event';
import BookingOutboxEventDetail from './booking-outbox-event-detail';
import BookingOutboxEventUpdate from './booking-outbox-event-update';
import BookingOutboxEventDeleteDialog from './booking-outbox-event-delete-dialog';

const BookingOutboxEventRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BookingOutboxEvent />} />
    <Route path="new" element={<BookingOutboxEventUpdate />} />
    <Route path=":id">
      <Route index element={<BookingOutboxEventDetail />} />
      <Route path="edit" element={<BookingOutboxEventUpdate />} />
      <Route path="delete" element={<BookingOutboxEventDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BookingOutboxEventRoutes;
