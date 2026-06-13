import { Link } from "react-router-dom";
import { AlertCircle } from "lucide-react";
import { Button } from "@/components/ui/button";

export function PublicEventError() {
  return (
    <div className="flex min-h-screen items-center justify-center bg-background p-4">
      <div className="flex w-full max-w-md flex-col items-center gap-4 text-center">
        <AlertCircle className="h-14 w-14 text-destructive" aria-hidden="true" />
        <div className="space-y-1.5">
          <h1 className="text-xl font-semibold tracking-tight">Link inválido</h1>
          <p className="text-sm text-muted-foreground">
            Não foi possível identificar o evento. Verifique o link e tente novamente.
          </p>
        </div>
        <Button asChild variant="outline" className="mt-2">
          <Link to="/">Voltar ao início</Link>
        </Button>
      </div>
    </div>
  );
}
