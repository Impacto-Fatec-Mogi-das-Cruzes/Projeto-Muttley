export interface EventListItem {
  id: string;
  title: string;
  startDate: string;
  endDate?: string | null;
  statusName: string;
  typeName: string;
  modalityName: string;
  points: number;
}
