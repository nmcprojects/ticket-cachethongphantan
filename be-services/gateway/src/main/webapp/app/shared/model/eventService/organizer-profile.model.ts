import dayjs from 'dayjs';

export interface IOrganizerProfile {
  id?: number;
  authUserId?: number;
  organizationName?: string;
  contactEmail?: string;
  contactPhone?: string | null;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IOrganizerProfile> = {};
