import type { ReactNode } from "react";
import { cn } from "@/lib/utils";

interface ViewFieldProps {
  label: string;
  value?: ReactNode;
  className?: string;
}

export function ViewField({ label, value, className }: ViewFieldProps) {
  const isEmpty =
    value === null || value === undefined || (typeof value === "string" && value.trim() === "");

  return (
    <div className={cn("space-y-1", className)}>
      <p className="text-xs text-muted-foreground">{label}</p>
      <div className="text-sm">{isEmpty ? "-" : value}</div>
    </div>
  );
}
