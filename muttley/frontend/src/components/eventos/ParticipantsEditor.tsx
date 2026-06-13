import { useEffect, useState } from "react";
import { Search } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { Checkbox } from "@/components/ui/checkbox";
import { Input } from "@/components/ui/input";
import { Skeleton } from "@/components/ui/skeleton";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { DataTablePagination } from "@/components/eventos/DataTablePagination";
import { useDebounce } from "@/hooks/use-debounce";
import { useParticipants } from "@/hooks/use-participants";
import { formatCpf } from "@/lib/cpf";
import { cn } from "@/lib/utils";
import type { Participant } from "@/models/participant/participant";

const PAGE_SIZE = 10;

interface ParticipantsEditorProps {
  organizers: Participant[];
  speakers: Participant[];
  sponsors: Participant[];
  onOrganizersChange: (value: Participant[]) => void;
  onSpeakersChange: (value: Participant[]) => void;
  onSponsorsChange: (value: Participant[]) => void;
}

const idsOf = (list: Participant[]) => list.map((p) => p.id);

export function ParticipantsEditor({
  organizers,
  speakers,
  sponsors,
  onOrganizersChange,
  onSpeakersChange,
  onSponsorsChange,
}: ParticipantsEditorProps) {
  return (
    <Tabs defaultValue="organizers" className="w-full">
      <TabsList className="grid w-full grid-cols-3">
        <TabsTrigger value="organizers" className="gap-1.5">
          Organizadores <Badge variant="secondary">{organizers.length}</Badge>
        </TabsTrigger>
        <TabsTrigger value="speakers" className="gap-1.5">
          Palestrantes <Badge variant="secondary">{speakers.length}</Badge>
        </TabsTrigger>
        <TabsTrigger value="sponsors" className="gap-1.5">
          Patrocinadores <Badge variant="secondary">{sponsors.length}</Badge>
        </TabsTrigger>
      </TabsList>

      <TabsContent value="organizers">
        <RoleParticipantPicker
          selected={organizers}
          onChange={onOrganizersChange}
          disabledIds={[...idsOf(speakers), ...idsOf(sponsors)]}
        />
      </TabsContent>
      <TabsContent value="speakers">
        <RoleParticipantPicker
          selected={speakers}
          onChange={onSpeakersChange}
          disabledIds={[...idsOf(organizers), ...idsOf(sponsors)]}
        />
      </TabsContent>
      <TabsContent value="sponsors">
        <RoleParticipantPicker
          selected={sponsors}
          onChange={onSponsorsChange}
          disabledIds={[...idsOf(organizers), ...idsOf(speakers)]}
        />
      </TabsContent>
    </Tabs>
  );
}

interface RoleParticipantPickerProps {
  selected: Participant[];
  onChange: (value: Participant[]) => void;
  disabledIds: string[];
}

function RoleParticipantPicker({ selected, onChange, disabledIds }: RoleParticipantPickerProps) {
  const [search, setSearch] = useState("");
  const debouncedSearch = useDebounce(search, 400);
  const [page, setPage] = useState(0);

  useEffect(() => {
    setPage(0);
  }, [debouncedSearch]);

  const { data, isLoading, isError, isFetching } = useParticipants({
    page,
    size: PAGE_SIZE,
    search: debouncedSearch || undefined,
  });

  const toggle = (participant: Participant) => {
    if (disabledIds.includes(participant.id)) return;
    onChange(
      selected.some((s) => s.id === participant.id)
        ? selected.filter((s) => s.id !== participant.id)
        : [...selected, participant],
    );
  };

  const participants = data?.content ?? [];

  return (
    <div className="space-y-3 pt-3">
      <div className="relative">
        <Search className="absolute left-2.5 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          className="pl-8"
          placeholder="Pesquisar por nome..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      <div className="min-h-[220px] space-y-1">
        {isLoading ? (
          Array.from({ length: 5 }).map((_, i) => <Skeleton key={i} className="h-14 w-full" />)
        ) : isError ? (
          <p className="py-10 text-center text-sm text-muted-foreground">
            Erro ao carregar participantes
          </p>
        ) : participants.length === 0 ? (
          <p className="py-10 text-center text-sm text-muted-foreground">
            Nenhum participante encontrado
          </p>
        ) : (
          participants.map((participant) => {
            const checked = selected.some((s) => s.id === participant.id);
            const disabled = disabledIds.includes(participant.id);
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
                  <p className="truncate text-sm font-semibold">{participant.name}</p>
                  <p className="truncate text-xs text-muted-foreground">
                    {participant.email ? `${participant.email} · ` : ""}
                    {formatCpf(participant.cpf)}
                  </p>
                </div>
                {checked && !disabled && <Badge variant="secondary">Selecionado</Badge>}
                {disabled && <Badge variant="outline">Em outro cargo</Badge>}
              </div>
            );
          })
        )}
      </div>

      {data && (
        <DataTablePagination
          page={data.number}
          totalPages={data.totalPages}
          totalElements={data.totalElements}
          itemLabel="participante"
          isFetching={isFetching}
          onPageChange={setPage}
        />
      )}
    </div>
  );
}
