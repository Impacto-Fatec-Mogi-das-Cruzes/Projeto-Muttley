import { useState } from "react";
import { Plus, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import type { Participant } from "@/models/participant/participant";
import { ParticipantPickerModal } from "./ParticipantPickerModal";

interface ResponsibleSectionProps {
  label: string;
  pickerTitle: string;
  selected: Participant[];
  onChange: (selected: Participant[]) => void;
  required?: boolean;
  excludeIds?: string[];
}

export function ResponsibleSection({
  label,
  pickerTitle,
  selected,
  onChange,
  required,
  excludeIds,
}: ResponsibleSectionProps) {
  const [open, setOpen] = useState(false);

  return (
    <div className="space-y-2 rounded-md border border-border p-3">
      <div className="flex items-center justify-between">
        <span className="text-sm font-medium">
          {label}
          {required && " *"}
        </span>
        <Button type="button" variant="outline" size="sm" onClick={() => setOpen(true)}>
          <Plus className="mr-1.5 h-4 w-4" /> Adicionar
        </Button>
      </div>

      {selected.length === 0 ? (
        <p className="text-xs text-muted-foreground">Nenhum selecionado.</p>
      ) : (
        <div className="flex flex-wrap gap-1.5">
          {selected.map((participant) => (
            <Badge key={participant.id} variant="secondary" className="gap-1">
              {participant.name}
              <button
                type="button"
                onClick={() => onChange(selected.filter((s) => s.id !== participant.id))}
                className="rounded-full transition-colors hover:text-destructive"
                aria-label={`Remover ${participant.name}`}
              >
                <X className="h-3 w-3" />
              </button>
            </Badge>
          ))}
        </div>
      )}

      <ParticipantPickerModal
        open={open}
        onOpenChange={setOpen}
        title={pickerTitle}
        selected={selected}
        onConfirm={onChange}
        excludeIds={excludeIds}
      />
    </div>
  );
}
