import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PaymentOutboxEvent from './payment-outbox-event';
import PaymentOutboxEventDetail from './payment-outbox-event-detail';
import PaymentOutboxEventUpdate from './payment-outbox-event-update';
import PaymentOutboxEventDeleteDialog from './payment-outbox-event-delete-dialog';

const PaymentOutboxEventRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PaymentOutboxEvent />} />
    <Route path="new" element={<PaymentOutboxEventUpdate />} />
    <Route path=":id">
      <Route index element={<PaymentOutboxEventDetail />} />
      <Route path="edit" element={<PaymentOutboxEventUpdate />} />
      <Route path="delete" element={<PaymentOutboxEventDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PaymentOutboxEventRoutes;
