import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/organizer-profile">
        <Translate contentKey="global.menu.entities.eventServiceOrganizerProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/event">
        <Translate contentKey="global.menu.entities.eventServiceEvent" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ticket-type">
        <Translate contentKey="global.menu.entities.eventServiceTicketType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ticket-reservation">
        <Translate contentKey="global.menu.entities.eventServiceTicketReservation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/booking">
        <Translate contentKey="global.menu.entities.bookingServiceBooking" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/booking-item">
        <Translate contentKey="global.menu.entities.bookingServiceBookingItem" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/booking-status-history">
        <Translate contentKey="global.menu.entities.bookingServiceBookingStatusHistory" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/booking-inbox-event">
        <Translate contentKey="global.menu.entities.bookingServiceBookingInboxEvent" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/booking-outbox-event">
        <Translate contentKey="global.menu.entities.bookingServiceBookingOutboxEvent" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment">
        <Translate contentKey="global.menu.entities.paymentServicePayment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment-attempt">
        <Translate contentKey="global.menu.entities.paymentServicePaymentAttempt" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment-webhook-log">
        <Translate contentKey="global.menu.entities.paymentServicePaymentWebhookLog" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment-outbox-event">
        <Translate contentKey="global.menu.entities.paymentServicePaymentOutboxEvent" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ticket">
        <Translate contentKey="global.menu.entities.ticketServiceTicket" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/checkin-log">
        <Translate contentKey="global.menu.entities.ticketServiceCheckinLog" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ticket-inbox-event">
        <Translate contentKey="global.menu.entities.ticketServiceTicketInboxEvent" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ticket-outbox-event">
        <Translate contentKey="global.menu.entities.ticketServiceTicketOutboxEvent" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/email-notification">
        <Translate contentKey="global.menu.entities.notificationServiceEmailNotification" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/email-notification-item">
        <Translate contentKey="global.menu.entities.notificationServiceEmailNotificationItem" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/notification-inbox-event">
        <Translate contentKey="global.menu.entities.notificationServiceNotificationInboxEvent" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
