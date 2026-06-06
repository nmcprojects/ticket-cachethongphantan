import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OrganizerProfile from './organizer-profile';
import OrganizerProfileDetail from './organizer-profile-detail';
import OrganizerProfileUpdate from './organizer-profile-update';
import OrganizerProfileDeleteDialog from './organizer-profile-delete-dialog';

const OrganizerProfileRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OrganizerProfile />} />
    <Route path="new" element={<OrganizerProfileUpdate />} />
    <Route path=":id">
      <Route index element={<OrganizerProfileDetail />} />
      <Route path="edit" element={<OrganizerProfileUpdate />} />
      <Route path="delete" element={<OrganizerProfileDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrganizerProfileRoutes;
