import { useEffect, useRef, useState } from "react";
import { Upload, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

interface ImageUploadFieldProps {
  value: File | null;
  onChange: (file: File | null) => void;
  id?: string;
  className?: string;
}

export function ImageUploadField({ value, onChange, id, className }: ImageUploadFieldProps) {
  const inputRef = useRef<HTMLInputElement>(null);
  const [preview, setPreview] = useState<string | null>(null);

  useEffect(() => {
    if (!value) {
      setPreview(null);
      return;
    }
    const url = URL.createObjectURL(value);
    setPreview(url);
    return () => URL.revokeObjectURL(url);
  }, [value]);

  const clear = () => {
    onChange(null);
    if (inputRef.current) inputRef.current.value = "";
  };

  return (
    <div className={cn("space-y-2", className)}>
      <input
        ref={inputRef}
        id={id}
        type="file"
        accept="image/*"
        className="hidden"
        onChange={(e) => onChange(e.target.files?.[0] ?? null)}
      />

      {preview ? (
        <div className="space-y-2">
          <div className="relative overflow-hidden rounded-md border border-border bg-muted/30">
            <img src={preview} alt="Pré-visualização" className="max-h-40 w-full object-contain" />
            <button
              type="button"
              onClick={clear}
              className="absolute right-2 top-2 rounded-full bg-background/80 p-1 text-foreground shadow transition-colors hover:bg-background"
              aria-label="Remover imagem"
            >
              <X className="h-4 w-4" />
            </button>
          </div>
          <Button type="button" variant="outline" size="sm" onClick={() => inputRef.current?.click()}>
            Trocar imagem
          </Button>
        </div>
      ) : (
        <button
          type="button"
          onClick={() => inputRef.current?.click()}
          className="flex w-full flex-col items-center justify-center gap-2 rounded-md border border-dashed border-border bg-muted/20 py-8 text-sm text-muted-foreground transition-colors hover:bg-muted/40"
        >
          <Upload className="h-5 w-5" />
          Clique para enviar uma imagem
        </button>
      )}
    </div>
  );
}
