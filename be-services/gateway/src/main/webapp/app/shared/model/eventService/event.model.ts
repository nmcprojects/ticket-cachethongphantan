import dayjs from 'dayjs';
import { IOrganizerProfile } from 'app/shared/model/eventService/organizer-profile.model';
import { EventStatus } from 'app/shared/model/enumerations/event-status.model';

export interface IEvent {
  id?: number;
  title?: string;
  description?: string | null;
  location?: string | null;
  startTime?: dayjs.Dayjs;
  endTime?: dayjs.Dayjs;
  bannerUrl?: string | null;
  status?: keyof typeof EventStatus;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  organizer?: IOrganizerProfile;
}

export const defaultValue: Readonly<IEvent> = {};
