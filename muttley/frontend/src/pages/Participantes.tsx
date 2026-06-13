import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { ArrowUpDown, Clock, Plus, Search, Users } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Skeleton } from "@/components/ui/skeleton";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import { DataTablePagination } from "@/components/eventos/DataTablePagination";
import { ParticipantCreateDialog } from "@/components/eventos/ParticipantCreateDialog";
import { useParticipants } from "@/hooks/use-participants";
import { useDebounce } from "@/hooks/use-debounce";
import { formatCpf } from "@/lib/cpf";

const PAGE_SIZE = 10;
const COLUMN_COUNT = 5;

type SortBy = "name" | "points";
type Direction = "asc" | "desc";

export default function Participantes() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const [createOpen, setCreateOpen] = useState(false);

  const page = Number(searchParams.get("page") ?? "0");
  const sortBy = (searchParams.get("sortBy") as SortBy) || "name";
  const direction = (searchParams.get("direction") as Direction) || "asc";
  const urlSearch = searchParams.get("search") ?? "";

  const [searchInput, setSearchInput] = useState(urlSearch);
  const debouncedSearch = useDebounce(searchInput, 500);

  useEffect(() => {
    if (debouncedSearch === urlSearch) return;
    setSearchParams(
      (prev) => {
        const next = new URLSearchParams(prev);
        if (debouncedSearch) next.set("search", debouncedSearch);
        else next.delete("search");
        next.set("page", "0");
        return next;
      },
      { replace: true },
    );
  }, [debouncedSearch, urlSearch, setSearchParams]);

  const { data, isLoading, isError, isFetching, refetch } = useParticipants({
    page,
    size: PAGE_SIZE,
    search: urlSearch || undefined,
    sortBy,
    direction,
  });

  const participants = data?.content ?? [];

  const handleSort = (column: SortBy) => {
    setSearchParams(
      (prev) => {
        const next = new URLSearchParams(prev);
        if (sortBy === column) {
          next.set("direction", direction === "asc" ? "desc" : "asc");
        } else {
          next.set("sortBy", column);
          next.set("direction", "asc");
        }
        next.set("page", "0");
        return next;
      },
      { replace: true },
    );
  };

  const handlePageChange = (nextPage: number) => {
    setSearchParams(
      (prev) => {
        const next = new URLSearchParams(prev);
        next.set("page", String(nextPage));
        return next;
      },
      { replace: true },
    );
  };

  const openHistory = (participantId: string) => {
    navigate(`/participantes/${participantId}/historico`, {
      state: { from: `/participantes?${searchParams.toString()}` },
    });
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <Users className="h-6 w-6 text-primary" />
          <div>
            <h1 className="text-2xl font-bold tracking-tight">Participantes</h1>
            <p className="text-sm text-muted-foreground">
              Gerencie os participantes cadastrados na plataforma.
            </p>
          </div>
        </div>
        <Button onClick={() => setCreateOpen(true)}>
          <Plus className="mr-1.5 h-4 w-4" /> Novo Participante
        </Button>
      </div>

      <div className="flex flex-wrap items-center gap-3">
        <div className="relative max-w-xs flex-1">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
            placeholder="Pesquisar por nome..."
            className="pl-9"
          />
        </div>
      </div>

      <div className="rounded-xl border border-border bg-card shadow-sm overflow-hidden">
        <Table>
          <TableHeader>
            <TableRow className="bg-muted/60">
              <TableHead>
                <button
                  type="button"
                  className="inline-flex items-center gap-1 font-medium"
                  onClick={() => handleSort("name")}
                >
                  Nome <ArrowUpDown className="h-3.5 w-3.5" />
                </button>
              </TableHead>
              <TableHead>E-mail</TableHead>
              <TableHead>CPF</TableHead>
              <TableHead>
                <button
                  type="button"
                  className="inline-flex items-center gap-1 font-medium"
                  onClick={() => handleSort("points")}
                >
                  Pontos <ArrowUpDown className="h-3.5 w-3.5" />
                </button>
              </TableHead>
              <TableHead className="text-right">Certificados</TableHead>
              <TableHead className="text-right">Medalhas</TableHead>
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
                  Não foi possível carregar os participantes. Tente novamente mais tarde.
                  <div className="mt-3">
                    <Button variant="outline" size="sm" onClick={() => refetch()}>
                      Tentar novamente
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ) : participants.length === 0 ? (
              <TableRow>
                <TableCell colSpan={COLUMN_COUNT} className="py-10 text-center text-sm text-muted-foreground">
                  {urlSearch
                    ? "Nenhum participante encontrado."
                    : "Nenhum participante cadastrado ainda."}
                </TableCell>
              </TableRow>
            ) : (
              participants.map((participant) => (
                <TableRow key={participant.id}>
                  <TableCell className="font-medium">{participant.name}</TableCell>
                  <TableCell className="text-muted-foreground">
                    <span className="block max-w-[250px] truncate" title={participant.email ?? ""}>
                      {participant.email ?? "—"}
                    </span>
                  </TableCell>
                  <TableCell>{formatCpf(participant.cpf)}</TableCell>
                  <TableCell>{participant.points ?? 0}</TableCell>
                  <TableCell className="text-center">{participant.certificates ?? 0}</TableCell>
                  <TableCell className="text-center">{participant.medals ?? 0}</TableCell>
                  <TableCell className="text-right">
                    <Tooltip>
                      <TooltipTrigger asChild>
                        <Button
                          variant="ghost"
                          size="icon"
                          aria-label="Ver histórico"
                          onClick={() => openHistory(participant.id)}
                        >
                          <Clock className="h-4 w-4" />
                        </Button>
                      </TooltipTrigger>
                      <TooltipContent>Ver histórico</TooltipContent>
                    </Tooltip>
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
          itemLabel="participante"
          isFetching={isFetching}
          onPageChange={handlePageChange}
        />
      )}

      <ParticipantCreateDialog open={createOpen} onOpenChange={setCreateOpen} onCreated={() => {}} />
    </div>
  );
}
