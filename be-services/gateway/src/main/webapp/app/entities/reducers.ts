import organizerProfile from 'app/entities/eventService/organizer-profile/organizer-profile.reducer';
import event from 'app/entities/eventService/event/event.reducer';
import ticketType from 'app/entities/eventService/ticket-type/ticket-type.reducer';
import ticketReservation from 'app/entities/eventService/ticket-reservation/ticket-reservation.reducer';
import booking from 'app/entities/bookingService/booking/booking.reducer';
import bookingItem from 'app/entities/bookingService/booking-item/booking-item.reducer';
import bookingStatusHistory from 'app/entities/bookingService/booking-status-history/booking-status-history.reducer';
import bookingInboxEvent from 'app/entities/bookingService/booking-inbox-event/booking-inbox-event.reducer';
import bookingOutboxEvent from 'app/entities/bookingService/booking-outbox-event/booking-outbox-event.reducer';
import payment from 'app/entities/paymentService/payment/payment.reducer';
import paymentAttempt from 'app/entities/paymentService/payment-attempt/payment-attempt.reducer';
import paymentWebhookLog from 'app/entities/paymentService/payment-webhook-log/payment-webhook-log.reducer';
import paymentOutboxEvent from 'app/entities/paymentService/payment-outbox-event/payment-outbox-event.reducer';
import ticket from 'app/entities/ticketService/ticket/ticket.reducer';
import checkinLog from 'app/entities/ticketService/checkin-log/checkin-log.reducer';
import ticketInboxEvent from 'app/entities/ticketService/ticket-inbox-event/ticket-inbox-event.reducer';
import ticketOutboxEvent from 'app/entities/ticketService/ticket-outbox-event/ticket-outbox-event.reducer';
import emailNotification from 'app/entities/notificationService/email-notification/email-notification.reducer';
import emailNotificationItem from 'app/entities/notificationService/email-notification-item/email-notification-item.reducer';
import notificationInboxEvent from 'app/entities/notificationService/notification-inbox-event/notification-inbox-event.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  organizerProfile,
  event,
  ticketType,
  ticketReservation,
  booking,
  bookingItem,
  bookingStatusHistory,
  bookingInboxEvent,
  bookingOutboxEvent,
  payment,
  paymentAttempt,
  paymentWebhookLog,
  paymentOutboxEvent,
  ticket,
  checkinLog,
  ticketInboxEvent,
  ticketOutboxEvent,
  emailNotification,
  emailNotificationItem,
  notificationInboxEvent,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
