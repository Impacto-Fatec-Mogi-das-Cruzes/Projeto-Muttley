import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { format, parseISO } from "date-fns";
import { Calendar, Plus, Eye } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

import { useEvents } from "@/hooks/use-events";
import { getEventStatusLabel, getEventStatusVariant } from "@/lib/event-status";
import { cn } from "@/lib/utils";

const PAGE_SIZE = 10;
const COLUMN_COUNT = 5;

type SortBy = "title" | "points";
type Direction = "asc" | "desc";

const formatDate = (iso?: string | null) => (iso ? format(parseISO(iso), "dd/MM/yyyy") : "");

const formatDateRange = (start: string, end?: string | null) => {
  const startLabel = formatDate(start);
  const endLabel = end ? formatDate(end) : "";
  return endLabel && endLabel !== startLabel ? `${startLabel} - ${endLabel}` : startLabel;
};

export default function Eventos() {
  const navigate = useNavigate();
  const [sortBy, setSortBy] = useState<SortBy>("title");
  const [direction, setDirection] = useState<Direction>("asc");
  const [page, setPage] = useState(0);

  const { data, isLoading, isError, isFetching } = useEvents({
    page,
    size: PAGE_SIZE,
    sortBy,
    direction,
  });

  const events = data?.content ?? [];

  const handleSortByChange = (value: SortBy) => {
    setSortBy(value);
    setPage(0);
  };

  const handleDirectionChange = (value: Direction) => {
    setDirection(value);
    setPage(0);
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <Calendar className="h-6 w-6 text-primary" />
          <h1 className="text-2xl font-bold tracking-tight">Eventos</h1>
        </div>
        <Button onClick={() => navigate("/eventos/novo")}>
          <Plus className="mr-1.5 h-4 w-4" /> Novo Evento
        </Button>
      </div>

      <div className="flex flex-wrap items-center gap-3">
        <span className="text-sm text-muted-foreground">Ordenar por</span>
        <Select value={sortBy} onValueChange={(value) => handleSortByChange(value as SortBy)}>
          <SelectTrigger className="w-40">
            <SelectValue />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="title">Título</SelectItem>
            <SelectItem value="points">Pontos</SelectItem>
          </SelectContent>
        </Select>
        <Select value={direction} onValueChange={(value) => handleDirectionChange(value as Direction)}>
          <SelectTrigger className="w-40">
            <SelectValue />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="asc">Crescente</SelectItem>
            <SelectItem value="desc">Decrescente</SelectItem>
          </SelectContent>
        </Select>
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
                  Não foi possível carregar os eventos. Tente novamente mais tarde.
                </TableCell>
              </TableRow>
            ) : events.length === 0 ? (
              <TableRow>
                <TableCell colSpan={COLUMN_COUNT} className="py-10 text-center text-sm text-muted-foreground">
                  Nenhum evento cadastrado ainda.
                </TableCell>
              </TableRow>
            ) : (
              events.map((event) => (
                <TableRow key={event.id}>
                  <TableCell className="font-medium">{event.title}</TableCell>
                  <TableCell className="text-muted-foreground">
                    {formatDateRange(event.startDate, event.endDate)}
                  </TableCell>
                  <TableCell>{event.modalityName}</TableCell>
                  <TableCell>
                    <span
                      className={cn(
                        "rounded-full border px-2 py-0.5 text-xs font-medium",
                        getEventStatusVariant(event.statusName),
                      )}
                    >
                      {getEventStatusLabel(event.statusName)}
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

      {data && data.totalPages > 1 && (
        <div className="flex items-center justify-between text-sm">
          <Button
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
  );
}
