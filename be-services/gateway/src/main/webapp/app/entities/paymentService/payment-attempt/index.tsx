import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PaymentAttempt from './payment-attempt';
import PaymentAttemptDetail from './payment-attempt-detail';
import PaymentAttemptUpdate from './payment-attempt-update';
import PaymentAttemptDeleteDialog from './payment-attempt-delete-dialog';

const PaymentAttemptRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PaymentAttempt />} />
    <Route path="new" element={<PaymentAttemptUpdate />} />
    <Route path=":id">
      <Route index element={<PaymentAttemptDetail />} />
      <Route path="edit" element={<PaymentAttemptUpdate />} />
      <Route path="delete" element={<PaymentAttemptDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PaymentAttemptRoutes;
