import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PaymentWebhookLog from './payment-webhook-log';
import PaymentWebhookLogDetail from './payment-webhook-log-detail';
import PaymentWebhookLogUpdate from './payment-webhook-log-update';
import PaymentWebhookLogDeleteDialog from './payment-webhook-log-delete-dialog';

const PaymentWebhookLogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PaymentWebhookLog />} />
    <Route path="new" element={<PaymentWebhookLogUpdate />} />
    <Route path=":id">
      <Route index element={<PaymentWebhookLogDetail />} />
      <Route path="edit" element={<PaymentWebhookLogUpdate />} />
      <Route path="delete" element={<PaymentWebhookLogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PaymentWebhookLogRoutes;
