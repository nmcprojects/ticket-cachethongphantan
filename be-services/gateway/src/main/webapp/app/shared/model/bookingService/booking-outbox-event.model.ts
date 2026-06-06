import dayjs from 'dayjs';
import { OutboxStatus } from 'app/shared/model/enumerations/outbox-status.model';

export interface IBookingOutboxEvent {
  id?: number;
  aggregateType?: string;
  aggregateId?: string;
  eventType?: string;
  payload?: string;
  status?: keyof typeof OutboxStatus;
  createdAt?: dayjs.Dayjs;
  publishedAt?: dayjs.Dayjs | null;
  errorMessage?: string | null;
}

export const defaultValue: Readonly<IBookingOutboxEvent> = {};
