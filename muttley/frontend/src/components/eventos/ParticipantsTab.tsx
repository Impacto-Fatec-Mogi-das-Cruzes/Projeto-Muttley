import { useMemo, useState } from "react";
import { format, parseISO } from "date-fns";
import { ptBR } from "date-fns/locale";
import { ArrowUpDown, Award, Users } from "lucide-react";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import { Skeleton } from "@/components/ui/skeleton";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { DataTablePagination } from "@/components/eventos/DataTablePagination";
import { RewardModal } from "@/components/eventos/RewardModal";
import { useEventDetails } from "@/hooks/use-event-details";
import { cn } from "@/lib/utils";

const PAGE_SIZE = 10;

type SortColumn = "name" | "registeredAt";
type Direction = "asc" | "desc";

const roleLabel: Record<string, string> = {
  ORGANIZER: "Organizador",
  SPEAKER: "Palestrante",
  SPONSOR: "Patrocinador",
  LISTENER: "Ouvinte",
  ATTENDEE: "Ouvinte",
  PARTICIPANT: "Participante",
};

const statusLabel: Record<string, string> = {
  INSCRITO: "Inscrito",
  PRESENTE: "Presente",
  AUSENTE: "Ausente",
};

const formatDateTime = (iso?: string | null) =>
  iso ? format(parseISO(iso), "dd/MM/yyyy HH:mm", { locale: ptBR }) : "-";

interface ParticipantsTabProps {
  eventId: string;
}

export function ParticipantsTab({ eventId }: ParticipantsTabProps) {
  const [page, setPage] = useState(0);
  const [sortBy, setSortBy] = useState<SortColumn>("name");
  const [direction, setDirection] = useState<Direction>("asc");
  const [selected, setSelected] = useState<string[]>([]);
  const [rewardOpen, setRewardOpen] = useState(false);

  const { data, isLoading, isError, isFetching, refetch } = useEventDetails(eventId, {
    page,
    size: PAGE_SIZE,
    sortBy,
    direction,
  });

  const attendees = useMemo(() => data?.participants.content ?? [], [data]);

  const handleSort = (column: SortColumn) => {
    if (sortBy === column) {
      setDirection((d) => (d === "asc" ? "desc" : "asc"));
    } else {
      setSortBy(column);
      setDirection(column === "name" ? "asc" : "desc");
    }
    setPage(0);
  };

  const toggle = (participantId: string) => {
    setSelected((prev) =>
      prev.includes(participantId)
        ? prev.filter((id) => id !== participantId)
        : [...prev, participantId],
    );
  };

  const pageIds = attendees.map((a) => a.participantId);
  const allPageSelected = pageIds.length > 0 && pageIds.every((id) => selected.includes(id));

  const toggleAllPage = () => {
    setSelected((prev) =>
      allPageSelected
        ? prev.filter((id) => !pageIds.includes(id))
        : Array.from(new Set([...prev, ...pageIds])),
    );
  };

  return (
    <div className="space-y-4">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <p className="text-sm text-muted-foreground">
          Inscritos/Presentes:{" "}
          <span className="font-medium text-foreground">
            {data?.registeredCount ?? 0}/{data?.presentCount ?? 0}
          </span>
        </p>
        <Button disabled={selected.length === 0} onClick={() => setRewardOpen(true)}>
          <Award className="mr-1.5 h-4 w-4" /> Recompensar ({selected.length})
        </Button>
      </div>

      <div className="bg-card border rounded-xl overflow-hidden">
        <Table>
          <TableHeader>
            <TableRow className="bg-muted/60">
              <TableHead className="w-12">
                <Checkbox
                  checked={allPageSelected}
                  onCheckedChange={toggleAllPage}
                  aria-label="Selecionar todos"
                  disabled={attendees.length === 0}
                />
              </TableHead>
              <TableHead>
                <button
                  type="button"
                  className="inline-flex items-center gap-1 font-medium"
                  onClick={() => handleSort("name")}
                >
                  Nome <ArrowUpDown className="h-3.5 w-3.5" />
                </button>
              </TableHead>
              <TableHead>Cargo</TableHead>
              <TableHead>
                <button
                  type="button"
                  className="inline-flex items-center gap-1 font-medium"
                  onClick={() => handleSort("registeredAt")}
                >
                  Data da Inscrição <ArrowUpDown className="h-3.5 w-3.5" />
                </button>
              </TableHead>
              <TableHead>Status</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {isLoading ? (
              Array.from({ length: 5 }).map((_, i) => (
                <TableRow key={i}>
                  {Array.from({ length: 5 }).map((__, j) => (
                    <TableCell key={j}>
                      <Skeleton className="h-5 w-full" />
                    </TableCell>
                  ))}
                </TableRow>
              ))
            ) : isError ? (
              <TableRow>
                <TableCell colSpan={5} className="py-10 text-center text-sm text-muted-foreground">
                  Não foi possível carregar os participantes.
                  <div className="mt-3">
                    <Button variant="outline" size="sm" onClick={() => refetch()}>
                      Tentar novamente
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ) : attendees.length === 0 ? (
              <TableRow>
                <TableCell colSpan={5} className="py-12 text-center">
                  <Users className="mx-auto mb-2 h-8 w-8 text-muted-foreground" />
                  <p className="text-sm text-muted-foreground">Nenhum participante inscrito.</p>
                </TableCell>
              </TableRow>
            ) : (
              attendees.map((attendee) => {
                const checked = selected.includes(attendee.participantId);
                return (
                  <TableRow key={attendee.id} className={cn(checked && "bg-primary/5")}>
                    <TableCell>
                      <Checkbox
                        checked={checked}
                        onCheckedChange={() => toggle(attendee.participantId)}
                        aria-label={`Selecionar ${attendee.name}`}
                      />
                    </TableCell>
                    <TableCell className="font-medium">{attendee.name}</TableCell>
                    <TableCell>{roleLabel[attendee.roleName] ?? attendee.roleName}</TableCell>
                    <TableCell className="text-muted-foreground">
                      {formatDateTime(attendee.registeredAt)}
                    </TableCell>
                    <TableCell>
                      <Badge variant={attendee.present ? "default" : "secondary"}>
                        {statusLabel[attendee.registrationStatus] ?? attendee.registrationStatus}
                      </Badge>
                    </TableCell>
                  </TableRow>
                );
              })
            )}
          </TableBody>
        </Table>
      </div>

      {data && (
        <DataTablePagination
          page={data.participants.number}
          totalPages={data.participants.totalPages}
          totalElements={data.participants.totalElements}
          itemLabel="participante"
          isFetching={isFetching}
          onPageChange={setPage}
        />
      )}

      <RewardModal
        open={rewardOpen}
        onOpenChange={setRewardOpen}
        eventId={eventId}
        participantIds={selected}
        onRewarded={() => setSelected([])}
      />
    </div>
  );
}
