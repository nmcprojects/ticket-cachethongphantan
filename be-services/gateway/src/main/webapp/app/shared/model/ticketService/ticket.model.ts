import dayjs from 'dayjs';
import { TicketStatus } from 'app/shared/model/enumerations/ticket-status.model';

export interface ITicket {
  id?: number;
  bookingId?: number;
  bookingItemId?: number;
  userId?: number;
  customerEmail?: string;
  eventId?: number;
  eventTitle?: string;
  ticketTypeId?: number;
  ticketTypeName?: string;
  ticketCode?: string;
  qrPayload?: string;
  status?: keyof typeof TicketStatus;
  issuedAt?: dayjs.Dayjs;
  checkedInAt?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ITicket> = {};
