import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TicketOutboxEvent from './ticket-outbox-event';
import TicketOutboxEventDetail from './ticket-outbox-event-detail';
import TicketOutboxEventUpdate from './ticket-outbox-event-update';
import TicketOutboxEventDeleteDialog from './ticket-outbox-event-delete-dialog';

const TicketOutboxEventRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TicketOutboxEvent />} />
    <Route path="new" element={<TicketOutboxEventUpdate />} />
    <Route path=":id">
      <Route index element={<TicketOutboxEventDetail />} />
      <Route path="edit" element={<TicketOutboxEventUpdate />} />
      <Route path="delete" element={<TicketOutboxEventDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TicketOutboxEventRoutes;
