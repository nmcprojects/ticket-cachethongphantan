import dayjs from 'dayjs';
import { ITicket } from 'app/shared/model/ticketService/ticket.model';
import { CheckinResult } from 'app/shared/model/enumerations/checkin-result.model';

export interface ICheckinLog {
  id?: number;
  ticketCode?: string;
  staffId?: number;
  eventId?: number;
  result?: keyof typeof CheckinResult;
  message?: string | null;
  checkedInAt?: dayjs.Dayjs;
  ticket?: ITicket | null;
}

export const defaultValue: Readonly<ICheckinLog> = {};
