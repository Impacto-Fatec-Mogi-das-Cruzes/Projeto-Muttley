export interface CertificateDTO {
  name: string;
  presentation: string;
  event: string;
  day: string;
  hours: number;
  responsible: string;
  responsibleDescription: string;
  backgroundImage: Buffer;
  signatureImage: Buffer;
}