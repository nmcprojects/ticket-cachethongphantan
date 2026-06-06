import dayjs from 'dayjs';
import { IBooking } from 'app/shared/model/bookingService/booking.model';

export interface IBookingItem {
  id?: number;
  ticketTypeId?: number;
  ticketTypeName?: string;
  quantity?: number;
  unitPrice?: number;
  totalPrice?: number;
  createdAt?: dayjs.Dayjs;
  booking?: IBooking;
}

export const defaultValue: Readonly<IBookingItem> = {};
