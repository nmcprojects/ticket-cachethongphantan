import dayjs from 'dayjs';
import { IPayment } from 'app/shared/model/paymentService/payment.model';
import { IPaymentAttempt } from 'app/shared/model/paymentService/payment-attempt.model';
import { PaymentProvider } from 'app/shared/model/enumerations/payment-provider.model';

export interface IPaymentWebhookLog {
  id?: number;
  provider?: keyof typeof PaymentProvider;
  providerEventId?: string | null;
  eventType?: string | null;
  rawPayload?: string;
  signature?: string | null;
  processed?: boolean;
  processingError?: string | null;
  receivedAt?: dayjs.Dayjs;
  processedAt?: dayjs.Dayjs | null;
  payment?: IPayment | null;
  attempt?: IPaymentAttempt | null;
}

export const defaultValue: Readonly<IPaymentWebhookLog> = {
  processed: false,
};
