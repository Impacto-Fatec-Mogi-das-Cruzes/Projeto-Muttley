import { Button } from "@/components/ui/button";

interface DataTablePaginationProps {
  page: number;
  totalPages: number;
  totalElements: number;
  itemLabel?: string;
  isFetching?: boolean;
  onPageChange: (page: number) => void;
}

export function DataTablePagination({
  page,
  totalPages,
  totalElements,
  itemLabel = "registro",
  isFetching,
  onPageChange,
}: DataTablePaginationProps) {
  const safeTotalPages = Math.max(1, totalPages);

  return (
    <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
      <p className="text-sm text-muted-foreground">
        Página {page + 1} de {safeTotalPages} · {totalElements} {itemLabel}(s)
      </p>
      <div className="flex items-center gap-2">
        <Button
          variant="outline"
          size="sm"
          disabled={page <= 0 || isFetching}
          onClick={() => onPageChange(Math.max(0, page - 1))}
        >
          Anterior
        </Button>
        <Button
          variant="outline"
          size="sm"
          disabled={page >= safeTotalPages - 1 || isFetching}
          onClick={() => onPageChange(page + 1)}
        >
          Próxima
        </Button>
      </div>
    </div>
  );
}
