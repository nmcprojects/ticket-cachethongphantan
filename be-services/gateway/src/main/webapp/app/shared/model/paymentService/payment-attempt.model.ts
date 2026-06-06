import dayjs from 'dayjs';
import { IPayment } from 'app/shared/model/paymentService/payment.model';
import { PaymentProvider } from 'app/shared/model/enumerations/payment-provider.model';
import { PaymentAttemptStatus } from 'app/shared/model/enumerations/payment-attempt-status.model';

export interface IPaymentAttempt {
  id?: number;
  provider?: keyof typeof PaymentProvider;
  providerCheckoutSessionId?: string | null;
  providerPaymentId?: string | null;
  checkoutUrl?: string | null;
  status?: keyof typeof PaymentAttemptStatus;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  payment?: IPayment;
}

export const defaultValue: Readonly<IPaymentAttempt> = {};
