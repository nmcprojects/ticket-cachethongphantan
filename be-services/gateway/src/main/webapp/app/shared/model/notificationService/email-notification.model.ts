import dayjs from 'dayjs';
import { NotificationProvider } from 'app/shared/model/enumerations/notification-provider.model';
import { NotificationStatus } from 'app/shared/model/enumerations/notification-status.model';

export interface IEmailNotification {
  id?: number;
  userId?: number;
  bookingId?: number;
  recipientEmail?: string;
  subject?: string;
  content?: string;
  provider?: keyof typeof NotificationProvider;
  status?: keyof typeof NotificationStatus;
  providerMessageId?: string | null;
  errorMessage?: string | null;
  sentAt?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IEmailNotification> = {};
