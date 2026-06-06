import dayjs from 'dayjs';
import { BookingStatus } from 'app/shared/model/enumerations/booking-status.model';

export interface IBooking {
  id?: number;
  userId?: number;
  customerEmail?: string;
  eventId?: number;
  eventTitle?: string;
  totalAmount?: number;
  currency?: string;
  status?: keyof typeof BookingStatus;
  paymentId?: number | null;
  paymentUrl?: string | null;
  expiredAt?: dayjs.Dayjs | null;
  paidAt?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IBooking> = {};
