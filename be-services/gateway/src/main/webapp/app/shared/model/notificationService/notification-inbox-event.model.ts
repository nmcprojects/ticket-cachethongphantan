import dayjs from 'dayjs';
import { EventProcessStatus } from 'app/shared/model/enumerations/event-process-status.model';

export interface INotificationInboxEvent {
  id?: number;
  sourceService?: string;
  eventId?: string;
  eventType?: string;
  payload?: string;
  status?: keyof typeof EventProcessStatus;
  receivedAt?: dayjs.Dayjs;
  processedAt?: dayjs.Dayjs | null;
  errorMessage?: string | null;
}

export const defaultValue: Readonly<INotificationInboxEvent> = {};
