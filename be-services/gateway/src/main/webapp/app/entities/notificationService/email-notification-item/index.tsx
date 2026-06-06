import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EmailNotificationItem from './email-notification-item';
import EmailNotificationItemDetail from './email-notification-item-detail';
import EmailNotificationItemUpdate from './email-notification-item-update';
import EmailNotificationItemDeleteDialog from './email-notification-item-delete-dialog';

const EmailNotificationItemRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EmailNotificationItem />} />
    <Route path="new" element={<EmailNotificationItemUpdate />} />
    <Route path=":id">
      <Route index element={<EmailNotificationItemDetail />} />
      <Route path="edit" element={<EmailNotificationItemUpdate />} />
      <Route path="delete" element={<EmailNotificationItemDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EmailNotificationItemRoutes;
