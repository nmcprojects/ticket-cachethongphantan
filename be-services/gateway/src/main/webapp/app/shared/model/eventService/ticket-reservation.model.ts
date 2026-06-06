import dayjs from 'dayjs';
import { ITicketType } from 'app/shared/model/eventService/ticket-type.model';
import { ReservationStatus } from 'app/shared/model/enumerations/reservation-status.model';

export interface ITicketReservation {
  id?: number;
  bookingId?: number;
  userId?: number;
  quantity?: number;
  status?: keyof typeof ReservationStatus;
  expiresAt?: dayjs.Dayjs;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  ticketType?: ITicketType;
}

export const defaultValue: Readonly<ITicketReservation> = {};
