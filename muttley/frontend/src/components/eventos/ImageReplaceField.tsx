import { useEffect, useRef, useState } from "react";
import { Upload, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

interface ImageReplaceFieldProps {
  currentUrl?: string | null;
  value: File | null;
  onChange: (file: File | null) => void;
  id?: string;
  className?: string;
  alt?: string;
  previewClassName?: string;
}

export function ImageReplaceField({
  currentUrl,
  value,
  onChange,
  id,
  className,
  alt = "Imagem",
  previewClassName = "max-h-24",
}: ImageReplaceFieldProps) {
  const inputRef = useRef<HTMLInputElement>(null);
  const [filePreview, setFilePreview] = useState<string | null>(null);

  useEffect(() => {
    if (!value) {
      setFilePreview(null);
      return;
    }
    const url = URL.createObjectURL(value);
    setFilePreview(url);
    return () => URL.revokeObjectURL(url);
  }, [value]);

  const preview = filePreview ?? currentUrl ?? null;

  const discardNew = () => {
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
        <div className="relative overflow-hidden rounded-md border border-dashed border-border bg-muted/30">
          <img src={preview} alt={alt} className={cn("w-full object-contain p-2", previewClassName)} />
        </div>
      ) : (
        <button
          type="button"
          onClick={() => inputRef.current?.click()}
          className="flex w-full flex-col items-center justify-center gap-2 rounded-md border border-dashed border-border bg-muted/20 py-6 text-sm text-muted-foreground transition-colors hover:bg-muted/40"
        >
          <Upload className="h-5 w-5" />
          Selecionar imagem
        </button>
      )}

      <div className="flex gap-2">
        <Button type="button" variant="outline" size="sm" onClick={() => inputRef.current?.click()}>
          {preview ? "Trocar imagem" : "Selecionar imagem"}
        </Button>
        {value && (
          <Button type="button" variant="ghost" size="sm" onClick={discardNew}>
            <X className="mr-1.5 h-4 w-4" /> Remover
          </Button>
        )}
      </div>
    </div>
  );
}
