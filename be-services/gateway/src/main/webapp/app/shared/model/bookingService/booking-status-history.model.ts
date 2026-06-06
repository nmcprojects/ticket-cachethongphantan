import dayjs from 'dayjs';
import { IBooking } from 'app/shared/model/bookingService/booking.model';
import { BookingStatus } from 'app/shared/model/enumerations/booking-status.model';

export interface IBookingStatusHistory {
  id?: number;
  oldStatus?: keyof typeof BookingStatus | null;
  newStatus?: keyof typeof BookingStatus;
  reason?: string | null;
  createdAt?: dayjs.Dayjs;
  booking?: IBooking;
}

export const defaultValue: Readonly<IBookingStatusHistory> = {};
