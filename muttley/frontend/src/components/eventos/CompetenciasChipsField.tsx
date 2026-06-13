import { useState, type KeyboardEvent } from "react";
import { X } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { cn } from "@/lib/utils";

interface CompetenciasChipsFieldProps {
  value: string[];
  onChange: (value: string[]) => void;
  placeholder?: string;
  id?: string;
  className?: string;
}

export function CompetenciasChipsField({
  value,
  onChange,
  placeholder = "Digite e pressione Enter",
  id,
  className,
}: CompetenciasChipsFieldProps) {
  const [draft, setDraft] = useState("");

  const add = (raw: string) => {
    const next = raw.trim();
    if (!next) return;
    if (value.some((v) => v.toLowerCase() === next.toLowerCase())) {
      setDraft("");
      return;
    }
    onChange([...value, next]);
    setDraft("");
  };

  const remove = (index: number) => {
    onChange(value.filter((_, i) => i !== index));
  };

  const handleKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter" || e.key === ",") {
      e.preventDefault();
      add(draft);
    } else if (e.key === "Backspace" && !draft && value.length) {
      remove(value.length - 1);
    }
  };

  return (
    <div className={cn("space-y-2", className)}>
      {value.length > 0 && (
        <div className="flex flex-wrap gap-1.5">
          {value.map((chip, index) => (
            <Badge key={`${chip}-${index}`} variant="secondary" className="gap-1">
              {chip}
              <button
                type="button"
                onClick={() => remove(index)}
                className="rounded-full transition-colors hover:text-destructive"
                aria-label={`Remover ${chip}`}
              >
                <X className="h-3 w-3" />
              </button>
            </Badge>
          ))}
        </div>
      )}
      <Input
        id={id}
        value={draft}
        placeholder={placeholder}
        onChange={(e) => setDraft(e.target.value)}
        onKeyDown={handleKeyDown}
        onBlur={() => add(draft)}
      />
    </div>
  );
}
