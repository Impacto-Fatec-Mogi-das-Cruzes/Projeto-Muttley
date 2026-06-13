export interface ParticipantEventHistoryItem {
  id: string;
  title: string;
  dateStart: string;
  dateEnd?: string | null;
  eventModality: { name: string };
  status: { name: string };
}
