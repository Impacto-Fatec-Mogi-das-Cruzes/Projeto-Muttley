export interface EventRequest {
  title: string;
  startDate: string;
  endDate: string;
  startHour: string;
  endHour: string;
  workload: number;
  capacity?: number;
  points: number;
  typeId: string;
  modalityId: string;
  addressOrLink: string;
  subject: string;
  keywords?: string;
  description?: string;
  organizerIds: string[];
  speakerIds: string[];
  sponsorIds: string[];
  nameSignature: string;
  positionSignature: string;
}
