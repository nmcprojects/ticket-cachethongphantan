import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EmailNotification from './email-notification';
import EmailNotificationDetail from './email-notification-detail';
import EmailNotificationUpdate from './email-notification-update';
import EmailNotificationDeleteDialog from './email-notification-delete-dialog';

const EmailNotificationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EmailNotification />} />
    <Route path="new" element={<EmailNotificationUpdate />} />
    <Route path=":id">
      <Route index element={<EmailNotificationDetail />} />
      <Route path="edit" element={<EmailNotificationUpdate />} />
      <Route path="delete" element={<EmailNotificationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EmailNotificationRoutes;
