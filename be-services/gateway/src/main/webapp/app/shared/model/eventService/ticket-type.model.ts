import dayjs from 'dayjs';
import { IEvent } from 'app/shared/model/eventService/event.model';
import { TicketTypeStatus } from 'app/shared/model/enumerations/ticket-type-status.model';

export interface ITicketType {
  id?: number;
  name?: string;
  description?: string | null;
  price?: number;
  currency?: string;
  totalQuantity?: number;
  availableQuantity?: number;
  reservedQuantity?: number;
  soldQuantity?: number;
  maxPerOrder?: number;
  status?: keyof typeof TicketTypeStatus;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  event?: IEvent;
}

export const defaultValue: Readonly<ITicketType> = {};
