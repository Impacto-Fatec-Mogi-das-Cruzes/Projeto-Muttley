import { useParams } from "react-router-dom";
import { PublicParticipationForm } from "@/components/public/PublicParticipationForm";
import { PublicEventError } from "@/components/public/PublicEventError";
import { useEventPresence } from "@/hooks/use-event-participation";

export default function EventPresence() {
  const { eventId } = useParams<{ eventId: string }>();
  const mutation = useEventPresence(eventId ?? "");

  if (!eventId) {
    return <PublicEventError />;
  }

  return (
    <PublicParticipationForm
      title="Formulário de Presença"
      successTitle="Presença confirmada com sucesso!"
      successDescription="Sua presença foi registrada com sucesso. Obrigado pela participação!"
      mutation={mutation}
    />
  );
}
