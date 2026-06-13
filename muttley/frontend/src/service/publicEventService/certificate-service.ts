import { publicApi } from "@/service/api";
import { unwrapData } from "@/service/response";
import type { CertificateData } from "@/models/public/certificate";

export async function getCertificate(certificateId: string): Promise<CertificateData> {
  const { data } = await publicApi.get(`public/certificate/${certificateId}`);
  return unwrapData<CertificateData>(data);
}
