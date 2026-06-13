import type { Page } from "@/models/common/page";

export interface EventParticipantLink {
  participantId: string;
  name: string;
  cpf: string;
  email?: string | null;
  roleName: string;
}

export interface EventAttendee {
  id: string;
  participantId: string;
  name: string;
  cpf: string;
  email?: string | null;
  roleName: string;
  registeredAt: string;
  checkInAt?: string | null;
  present: boolean;
  registrationStatus: string;
}

export interface EventDetails {
  id: string;
  title: string;
  startDate: string;
  endDate?: string | null;
  startHour?: string | null;
  endHour?: string | null;
  workload: number;
  points: number;
  typeId: string;
  typeName: string;
  subject: string;
  keywords?: string | null;
  description?: string | null;
  modalityId: string;
  modalityName: string;
  addressOrLink: string;
  capacity?: number | null;
  organizers: EventParticipantLink[];
  speakers: EventParticipantLink[];
  sponsors: EventParticipantLink[];
  imageBackgroundUrl?: string | null;
  signatureImageUrl?: string | null;
  nameSignature: string;
  positionSignature: string;
  statusId: string;
  statusName: string;
  registrationUrl: string;
  presenceUrl: string;
  registrationQrCodeUrl: string;
  presenceQrCodeUrl: string;
  registeredCount: number;
  presentCount: number;
  participants: Page<EventAttendee>;
  createdAt: string;
  updatedAt: string;
}
