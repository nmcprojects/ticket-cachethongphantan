import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TicketInboxEvent from './ticket-inbox-event';
import TicketInboxEventDetail from './ticket-inbox-event-detail';
import TicketInboxEventUpdate from './ticket-inbox-event-update';
import TicketInboxEventDeleteDialog from './ticket-inbox-event-delete-dialog';

const TicketInboxEventRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TicketInboxEvent />} />
    <Route path="new" element={<TicketInboxEventUpdate />} />
    <Route path=":id">
      <Route index element={<TicketInboxEventDetail />} />
      <Route path="edit" element={<TicketInboxEventUpdate />} />
      <Route path="delete" element={<TicketInboxEventDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TicketInboxEventRoutes;
