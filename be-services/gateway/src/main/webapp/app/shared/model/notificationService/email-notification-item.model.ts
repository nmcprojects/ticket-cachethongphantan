import dayjs from 'dayjs';
import { IEmailNotification } from 'app/shared/model/notificationService/email-notification.model';

export interface IEmailNotificationItem {
  id?: number;
  ticketId?: number;
  ticketCode?: string;
  createdAt?: dayjs.Dayjs;
  notification?: IEmailNotification;
}

export const defaultValue: Readonly<IEmailNotificationItem> = {};
