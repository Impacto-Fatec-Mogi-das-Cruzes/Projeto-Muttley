import { Copy, Download } from "lucide-react";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import type { EventDetails } from "@/models/event/event-details";

interface LinksTabProps {
  details: EventDetails;
}

export function LinksTab({ details }: LinksTabProps) {
  return (
    <div className="grid gap-4 sm:grid-cols-2">
      <LinkCard
        title="QR Code de Inscrição"
        qrUrl={details.registrationQrCodeUrl}
        linkUrl={details.registrationUrl}
        fileName="qrcode-inscricao.png"
      />
      <LinkCard
        title="QR Code de Presença"
        qrUrl={details.presenceQrCodeUrl}
        linkUrl={details.presenceUrl}
        fileName="qrcode-presenca.png"
      />
    </div>
  );
}

interface LinkCardProps {
  title: string;
  qrUrl: string;
  linkUrl: string;
  fileName: string;
}

function LinkCard({ title, qrUrl, linkUrl, fileName }: LinkCardProps) {
  const copyLink = async () => {
    try {
      await navigator.clipboard.writeText(linkUrl);
      toast.success("Link copiado para a área de transferência.");
    } catch {
      toast.error("Não foi possível copiar o link.");
    }
  };

  const downloadQr = async () => {
    try {
      const response = await fetch(qrUrl);
      const blob = await response.blob();
      const url = URL.createObjectURL(blob);
      const anchor = document.createElement("a");
      anchor.href = url;
      anchor.download = fileName;
      anchor.click();
      URL.revokeObjectURL(url);
    } catch {
      window.open(qrUrl, "_blank", "noopener,noreferrer");
    }
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle className="text-base">{title}</CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="flex justify-center rounded-md bg-white p-4">
          <img src={qrUrl} alt={title} className="h-44 w-44 object-contain" />
        </div>
        <a
          href={linkUrl}
          target="_blank"
          rel="noreferrer"
          className="block text-sm text-primary hover:underline break-all"
        >
          {linkUrl}
        </a>
        <div className="flex flex-wrap gap-2">
          <Button variant="outline" size="sm" onClick={copyLink}>
            <Copy className="mr-1.5 h-4 w-4" /> Copiar Link
          </Button>
          <Button variant="outline" size="sm" onClick={downloadQr}>
            <Download className="mr-1.5 h-4 w-4" /> Baixar QR
          </Button>
        </div>
      </CardContent>
    </Card>
  );
}
