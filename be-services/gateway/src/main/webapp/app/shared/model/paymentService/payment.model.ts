import dayjs from 'dayjs';
import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';

export interface IPayment {
  id?: number;
  bookingId?: number;
  userId?: number;
  amount?: number;
  currency?: string;
  status?: keyof typeof PaymentStatus;
  paidAt?: dayjs.Dayjs | null;
  failedAt?: dayjs.Dayjs | null;
  expiredAt?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IPayment> = {};
