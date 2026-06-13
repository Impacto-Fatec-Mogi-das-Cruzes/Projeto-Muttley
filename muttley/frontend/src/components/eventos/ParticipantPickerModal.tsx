import { useEffect, useState } from "react";
import { Plus, Search } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Skeleton } from "@/components/ui/skeleton";
import { Checkbox } from "@/components/ui/checkbox";
import { useDebounce } from "@/hooks/use-debounce";
import { useParticipants } from "@/hooks/use-participants";
import { formatCpf } from "@/lib/cpf";
import { cn } from "@/lib/utils";
import type { Participant } from "@/models/participant/participant";
import { ParticipantCreateDialog } from "./ParticipantCreateDialog";

const PAGE_SIZE = 10;

interface ParticipantPickerModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  title: string;
  selected: Participant[];
  onConfirm: (selected: Participant[]) => void;
  excludeIds?: string[];
}

export function ParticipantPickerModal({
  open,
  onOpenChange,
  title,
  selected,
  onConfirm,
  excludeIds,
}: ParticipantPickerModalProps) {
  const [search, setSearch] = useState("");
  const debouncedSearch = useDebounce(search, 400);
  const [page, setPage] = useState(0);
  const [working, setWorking] = useState<Participant[]>(selected);
  const [createOpen, setCreateOpen] = useState(false);

  useEffect(() => {
    if (open) {
      setWorking(selected);
      setSearch("");
      setPage(0);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [open]);

  useEffect(() => {
    setPage(0);
  }, [debouncedSearch]);

  const { data, isLoading, isError, isFetching } = useParticipants(
    { page, size: PAGE_SIZE, search: debouncedSearch || undefined },
    open,
  );

  const toggle = (participant: Participant) => {
    if (excludeIds?.includes(participant.id)) return;
    setWorking((prev) =>
      prev.some((w) => w.id === participant.id)
        ? prev.filter((w) => w.id !== participant.id)
        : [...prev, participant],
    );
  };

  const handleCreated = (participant: Participant) => {
    setWorking((prev) =>
      prev.some((w) => w.id === participant.id) ? prev : [...prev, participant],
    );
  };

  const participants = data?.content ?? [];

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-lg">
        <DialogHeader>
          <DialogTitle>{title}</DialogTitle>
        </DialogHeader>

        <div className="space-y-3">
          <div className="flex items-center gap-2">
            <div className="relative flex-1">
              <Search className="absolute left-2.5 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
              <Input
                className="pl-8"
                placeholder="Pesquisar por nome..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>
            <Button type="button" variant="outline" onClick={() => setCreateOpen(true)}>
              <Plus className="mr-1.5 h-4 w-4" /> Novo
            </Button>
          </div>

          <p className="text-xs text-muted-foreground">{working.length} selecionado(s)</p>

          <div className="min-h-[260px] space-y-1">
            {isLoading ? (
              Array.from({ length: 5 }).map((_, i) => <Skeleton key={i} className="h-12 w-full" />)
            ) : isError ? (
              <p className="py-10 text-center text-sm text-muted-foreground">
                Não foi possível carregar os participantes.
              </p>
            ) : participants.length === 0 ? (
              <p className="py-10 text-center text-sm text-muted-foreground">
                {debouncedSearch ? "Nenhum participante encontrado." : "Nenhum participante cadastrado."}
              </p>
            ) : (
              participants.map((participant) => {
                const checked = working.some((w) => w.id === participant.id);
                const disabled = excludeIds?.includes(participant.id) ?? false;
                return (
                  <div
                    key={participant.id}
                    role="button"
                    tabIndex={disabled ? -1 : 0}
                    aria-disabled={disabled}
                    onClick={() => toggle(participant)}
                    onKeyDown={(e) => {
                      if (e.key === "Enter" || e.key === " ") {
                        e.preventDefault();
                        toggle(participant);
                      }
                    }}
                    className={cn(
                      "flex w-full items-center gap-3 rounded-md border px-3 py-2 text-left transition-colors",
                      disabled
                        ? "cursor-not-allowed border-border opacity-60"
                        : "cursor-pointer hover:bg-accent",
                      checked && !disabled ? "border-primary bg-primary/5" : "border-border",
                    )}
                  >
                    <Checkbox checked={checked} disabled={disabled} className="pointer-events-none" />
                    <div className="min-w-0 flex-1">
                      <p className="truncate text-sm font-medium">{participant.name}</p>
                      <p className="truncate text-xs text-muted-foreground">{formatCpf(participant.cpf)}</p>
                    </div>
                    {disabled && <Badge variant="outline">Em outro cargo</Badge>}
                  </div>
                );
              })
            )}
          </div>

          {data && data.totalPages > 1 && (
            <div className="flex items-center justify-between text-sm">
              <Button
                type="button"
                variant="outline"
                size="sm"
                disabled={data.first || isFetching}
                onClick={() => setPage((p) => Math.max(0, p - 1))}
              >
                Anterior
              </Button>
              <span className="text-muted-foreground">
                Página {data.number + 1} de {data.totalPages}
              </span>
              <Button
                type="button"
                variant="outline"
                size="sm"
                disabled={data.last || isFetching}
                onClick={() => setPage((p) => p + 1)}
              >
                Próxima
              </Button>
            </div>
          )}
        </div>

        <DialogFooter>
          <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>
            Cancelar
          </Button>
          <Button
            type="button"
            onClick={() => {
              onConfirm(working);
              onOpenChange(false);
            }}
          >
            Confirmar ({working.length})
          </Button>
        </DialogFooter>

        <ParticipantCreateDialog open={createOpen} onOpenChange={setCreateOpen} onCreated={handleCreated} />
      </DialogContent>
    </Dialog>
  );
}
