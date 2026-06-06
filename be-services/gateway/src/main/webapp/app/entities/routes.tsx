import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';

import OrganizerProfile from './eventService/organizer-profile';
import Event from './eventService/event';
import TicketType from './eventService/ticket-type';
import TicketReservation from './eventService/ticket-reservation';
import Booking from './bookingService/booking';
import BookingItem from './bookingService/booking-item';
import BookingStatusHistory from './bookingService/booking-status-history';
import BookingInboxEvent from './bookingService/booking-inbox-event';
import BookingOutboxEvent from './bookingService/booking-outbox-event';
import Payment from './paymentService/payment';
import PaymentAttempt from './paymentService/payment-attempt';
import PaymentWebhookLog from './paymentService/payment-webhook-log';
import PaymentOutboxEvent from './paymentService/payment-outbox-event';
import Ticket from './ticketService/ticket';
import CheckinLog from './ticketService/checkin-log';
import TicketInboxEvent from './ticketService/ticket-inbox-event';
import TicketOutboxEvent from './ticketService/ticket-outbox-event';
import EmailNotification from './notificationService/email-notification';
import EmailNotificationItem from './notificationService/email-notification-item';
import NotificationInboxEvent from './notificationService/notification-inbox-event';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('gateway', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="organizer-profile/*" element={<OrganizerProfile />} />
        <Route path="event/*" element={<Event />} />
        <Route path="ticket-type/*" element={<TicketType />} />
        <Route path="ticket-reservation/*" element={<TicketReservation />} />
        <Route path="booking/*" element={<Booking />} />
        <Route path="booking-item/*" element={<BookingItem />} />
        <Route path="booking-status-history/*" element={<BookingStatusHistory />} />
        <Route path="booking-inbox-event/*" element={<BookingInboxEvent />} />
        <Route path="booking-outbox-event/*" element={<BookingOutboxEvent />} />
        <Route path="payment/*" element={<Payment />} />
        <Route path="payment-attempt/*" element={<PaymentAttempt />} />
        <Route path="payment-webhook-log/*" element={<PaymentWebhookLog />} />
        <Route path="payment-outbox-event/*" element={<PaymentOutboxEvent />} />
        <Route path="ticket/*" element={<Ticket />} />
        <Route path="checkin-log/*" element={<CheckinLog />} />
        <Route path="ticket-inbox-event/*" element={<TicketInboxEvent />} />
        <Route path="ticket-outbox-event/*" element={<TicketOutboxEvent />} />
        <Route path="email-notification/*" element={<EmailNotification />} />
        <Route path="email-notification-item/*" element={<EmailNotificationItem />} />
        <Route path="notification-inbox-event/*" element={<NotificationInboxEvent />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
