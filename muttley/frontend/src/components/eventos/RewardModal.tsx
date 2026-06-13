import { useEffect, useState } from "react";
import { Loader2 } from "lucide-react";
import { toast } from "sonner";

import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { CompetenciasChipsField } from "@/components/eventos/CompetenciasChipsField";
import { useRewardParticipants } from "@/hooks/use-reward-participants";
import { getApiErrorMessage } from "@/lib/api-error";

interface RewardModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  eventId: string;
  participantIds: string[];
  onRewarded: () => void;
}

const MAX_DESCRIPTION = 50;

export function RewardModal({
  open,
  onOpenChange,
  eventId,
  participantIds,
  onRewarded,
}: RewardModalProps) {
  const [description, setDescription] = useState("");
  const [competencias, setCompetencias] = useState<string[]>([]);
  const reward = useRewardParticipants();

  useEffect(() => {
    if (open) {
      setDescription("");
      setCompetencias([]);
    }
  }, [open]);

  const canSubmit =
    description.trim().length > 0 && competencias.length > 0 && !reward.isPending;

  const handleConfirm = async () => {
    if (!canSubmit) return;
    try {
      await reward.mutateAsync({
        eventId,
        payload: {
          participantIds,
          description: description.trim(),
          competencies: competencias.join(","),
        },
      });
      toast.success(`Recompensa enviada para ${participantIds.length} participante(s).`);
      onRewarded();
      onOpenChange(false);
    } catch (error) {
      toast.error(getApiErrorMessage(error, "Não foi possível recompensar os participantes."));
    }
  };

  return (
    <Dialog open={open} onOpenChange={(next) => !reward.isPending && onOpenChange(next)}>
      <DialogContent className="max-w-lg">
        <DialogHeader>
          <DialogTitle>Recompensar participantes</DialogTitle>
          <DialogDescription>
            {participantIds.length} participante(s) selecionado(s).
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="reward-description">Descrição da Medalha</Label>
            <Input
              id="reward-description"
              maxLength={MAX_DESCRIPTION}
              placeholder="Ex: Destaque em Inovação"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
            <p className="text-right text-xs text-muted-foreground">
              {description.length}/{MAX_DESCRIPTION}
            </p>
          </div>

          <div className="space-y-2">
            <Label>Competências</Label>
            <CompetenciasChipsField value={competencias} onChange={setCompetencias} />
          </div>
        </div>

        <DialogFooter>
          <Button variant="outline" disabled={reward.isPending} onClick={() => onOpenChange(false)}>
            Cancelar
          </Button>
          <Button disabled={!canSubmit} onClick={handleConfirm}>
            {reward.isPending && <Loader2 className="mr-1.5 h-4 w-4 animate-spin" />}
            Recompensar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
