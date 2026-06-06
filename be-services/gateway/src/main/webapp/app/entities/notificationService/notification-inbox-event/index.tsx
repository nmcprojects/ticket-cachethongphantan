import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import NotificationInboxEvent from './notification-inbox-event';
import NotificationInboxEventDetail from './notification-inbox-event-detail';
import NotificationInboxEventUpdate from './notification-inbox-event-update';
import NotificationInboxEventDeleteDialog from './notification-inbox-event-delete-dialog';

const NotificationInboxEventRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<NotificationInboxEvent />} />
    <Route path="new" element={<NotificationInboxEventUpdate />} />
    <Route path=":id">
      <Route index element={<NotificationInboxEventDetail />} />
      <Route path="edit" element={<NotificationInboxEventUpdate />} />
      <Route path="delete" element={<NotificationInboxEventDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default NotificationInboxEventRoutes;
