import { useQuery } from "@tanstack/react-query";
import { getCertificate } from "@/service/publicEventService/certificate-service";

export function useCertificate(certificateId: string | undefined) {
  return useQuery({
    queryKey: ["certificate", certificateId],
    queryFn: () => getCertificate(certificateId as string),
    enabled: !!certificateId,
  });
}
