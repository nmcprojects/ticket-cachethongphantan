import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TicketReservation from './ticket-reservation';
import TicketReservationDetail from './ticket-reservation-detail';
import TicketReservationUpdate from './ticket-reservation-update';
import TicketReservationDeleteDialog from './ticket-reservation-delete-dialog';

const TicketReservationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TicketReservation />} />
    <Route path="new" element={<TicketReservationUpdate />} />
    <Route path=":id">
      <Route index element={<TicketReservationDetail />} />
      <Route path="edit" element={<TicketReservationUpdate />} />
      <Route path="delete" element={<TicketReservationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TicketReservationRoutes;
