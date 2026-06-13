import { useParams } from "react-router-dom";
import { Loader2, ShieldAlert } from "lucide-react";
import { useCertificate } from "@/hooks/use-certificate";

export default function CertificadoPublico() {
  const { id } = useParams<{ id: string }>();
  const { data, isLoading, isError } = useCertificate(id);

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center p-6">
        <Loader2 className="h-10 w-10 animate-spin text-muted-foreground" />
      </div>
    );
  }

  if (isError || !data) {
    return (
      <div className="min-h-screen flex items-center justify-center p-6">
        <div className="text-center space-y-3">
          <ShieldAlert className="mx-auto h-16 w-16 text-destructive" />
          <h1 className="text-2xl font-bold">Certificado não encontrado ou inválido.</h1>
          <p className="text-muted-foreground">Verifique o código informado.</p>
        </div>
      </div>
    );
  }

  return (
    <iframe
      src={data.certificateUrl}
      title="Certificado"
      className="h-screen w-full border-0"
    />
  );
}
