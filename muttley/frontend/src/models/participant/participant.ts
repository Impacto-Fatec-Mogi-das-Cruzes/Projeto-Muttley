export interface Participant {
  id: string;
  name: string;
  cpf: string;
  email?: string;
  points?: number;
  certificates?: number;
  medals?: number;
}
