import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BookingItem from './booking-item';
import BookingItemDetail from './booking-item-detail';
import BookingItemUpdate from './booking-item-update';
import BookingItemDeleteDialog from './booking-item-delete-dialog';

const BookingItemRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BookingItem />} />
    <Route path="new" element={<BookingItemUpdate />} />
    <Route path=":id">
      <Route index element={<BookingItemDetail />} />
      <Route path="edit" element={<BookingItemUpdate />} />
      <Route path="delete" element={<BookingItemDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BookingItemRoutes;
