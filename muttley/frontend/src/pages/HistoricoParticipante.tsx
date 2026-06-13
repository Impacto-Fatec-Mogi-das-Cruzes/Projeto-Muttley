import { useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { format, parseISO } from "date-fns";
import { ArrowLeft, CalendarX, Eye } from "lucide-react";

import { Button } from "@/components/ui/button";
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
import { useParticipantHistory } from "@/hooks/use-participants";
import { getEventStatusLabel, getEventStatusVariant } from "@/lib/event-status";
import { cn } from "@/lib/utils";

const PAGE_SIZE = 20;
const COLUMN_COUNT = 5;

const formatDate = (iso?: string | null) => (iso ? format(parseISO(iso), "dd/MM/yyyy") : "");

const formatDateRange = (start: string, end?: string | null) => {
  const startLabel = formatDate(start);
  const endLabel = end ? formatDate(end) : "";
  return endLabel && endLabel !== startLabel ? `${startLabel} - ${endLabel}` : startLabel;
};

export default function HistoricoParticipante() {
  const navigate = useNavigate();
  const location = useLocation();
  const { id } = useParams<{ id: string }>();
  const [page, setPage] = useState(0);

  const backTo = (location.state as { from?: string } | null)?.from ?? "/participantes";

  const { data, isLoading, isError, isFetching, refetch } = useParticipantHistory(id, {
    page,
    size: PAGE_SIZE,
  });

  const events = data?.content ?? [];

  return (
    <div className="space-y-6">
      <div className="space-y-3">
        <Button
          variant="ghost"
          size="sm"
          className="-ml-2 text-muted-foreground hover:text-foreground"
          onClick={() => navigate(backTo)}
        >
          <ArrowLeft className="mr-1 h-4 w-4" /> Voltar
        </Button>
        <div>
          <h1 className="text-2xl font-bold tracking-tight">Histórico de Participação</h1>
          <p className="text-sm text-muted-foreground">
            Eventos nos quais este participante realizou inscrição ou presença.
          </p>
        </div>
      </div>

      <div className="rounded-xl border border-border bg-card shadow-sm overflow-hidden">
        <Table>
          <TableHeader>
            <TableRow className="bg-muted/60">
              <TableHead>Título</TableHead>
              <TableHead>Data</TableHead>
              <TableHead>Modalidade</TableHead>
              <TableHead>Status</TableHead>
              <TableHead className="text-right">Ações</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {isLoading ? (
              Array.from({ length: 5 }).map((_, i) => (
                <TableRow key={i}>
                  {Array.from({ length: COLUMN_COUNT }).map((__, j) => (
                    <TableCell key={j}>
                      <Skeleton className="h-5 w-full" />
                    </TableCell>
                  ))}
                </TableRow>
              ))
            ) : isError ? (
              <TableRow>
                <TableCell colSpan={COLUMN_COUNT} className="py-10 text-center text-sm text-muted-foreground">
                  Não foi possível carregar o histórico do participante.
                  <div className="mt-3">
                    <Button variant="outline" size="sm" onClick={() => refetch()}>
                      Tentar novamente
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ) : events.length === 0 ? (
              <TableRow>
                <TableCell colSpan={COLUMN_COUNT} className="py-12 text-center">
                  <CalendarX className="mx-auto mb-2 h-8 w-8 text-muted-foreground" />
                  <p className="text-sm text-muted-foreground">
                    Nenhum evento encontrado para este participante.
                  </p>
                </TableCell>
              </TableRow>
            ) : (
              events.map((event) => (
                <TableRow key={event.id}>
                  <TableCell className="font-medium">{event.title}</TableCell>
                  <TableCell className="text-muted-foreground">
                    {formatDateRange(event.dateStart, event.dateEnd)}
                  </TableCell>
                  <TableCell>{event.eventModality?.name ?? "—"}</TableCell>
                  <TableCell>
                    <span
                      className={cn(
                        "rounded-full border px-2 py-0.5 text-xs font-medium",
                        getEventStatusVariant(event.status?.name ?? ""),
                      )}
                    >
                      {getEventStatusLabel(event.status?.name ?? "")}
                    </span>
                  </TableCell>
                  <TableCell className="text-right">
                    <Button
                      variant="ghost"
                      size="icon"
                      aria-label="Ver detalhes"
                      onClick={() => navigate(`/eventos/${event.id}`)}
                    >
                      <Eye className="h-4 w-4" />
                    </Button>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </div>

      {data && (
        <DataTablePagination
          page={data.number}
          totalPages={data.totalPages}
          totalElements={data.totalElements}
          itemLabel="evento"
          isFetching={isFetching}
          onPageChange={setPage}
        />
      )}
    </div>
  );
}
