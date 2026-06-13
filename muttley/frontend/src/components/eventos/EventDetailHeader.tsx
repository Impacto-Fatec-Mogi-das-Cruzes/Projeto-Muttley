import { Link } from "react-router-dom";
import { ArrowLeft, Check, Loader2, Pencil, Save, Trash } from "lucide-react";
import { Button } from "@/components/ui/button";
import { EventStatusBadge } from "@/components/eventos/EventStatusBadge";
import { cn } from "@/lib/utils";

interface EventDetailHeaderProps {
  title: string;
  statusName: string;
  mode: "view" | "edit";
  isFinished: boolean;
  saving?: boolean;
  onEdit: () => void;
  onDelete: () => void;
  onFinalize: () => void;
  onCancel: () => void;
  onSave: () => void;
}

export function EventDetailHeader({
  title,
  statusName,
  mode,
  isFinished,
  saving,
  onEdit,
  onDelete,
  onFinalize,
  onCancel,
  onSave,
}: EventDetailHeaderProps) {
  return (
    <header className="flex flex-wrap items-center justify-between gap-4">
      <div className="flex min-w-0 items-center gap-3">
        <Button asChild variant="ghost" size="icon">
          <Link to="/eventos" aria-label="Voltar">
            <ArrowLeft className="h-5 w-5" />
          </Link>
        </Button>
        <h1 className="truncate text-xl font-semibold">{title}</h1>
        <EventStatusBadge status={statusName} />
      </div>

      <div className="flex flex-wrap items-center gap-2">
        {mode === "view" ? (
          <>
            <Button variant="outline" onClick={onEdit}>
              <Pencil className="mr-1.5 h-4 w-4" /> Editar Evento
            </Button>
            <Button
              variant="outline"
              className="text-destructive hover:text-destructive"
              onClick={onDelete}
            >
              <Trash className="mr-1.5 h-4 w-4" /> Excluir Evento
            </Button>
            {!isFinished && (
              <Button onClick={onFinalize}>
                <Check className="mr-1.5 h-4 w-4" /> Finalizar Evento
              </Button>
            )}
          </>
        ) : (
          <>
            <Button variant="outline" onClick={onCancel} disabled={saving}>
              Cancelar
            </Button>
            <Button onClick={onSave} disabled={saving}>
              {saving ? (
                <Loader2 className={cn("mr-1.5 h-4 w-4 animate-spin")} />
              ) : (
                <Save className="mr-1.5 h-4 w-4" />
              )}
              Salvar Alterações
            </Button>
          </>
        )}
      </div>
    </header>
  );
}
