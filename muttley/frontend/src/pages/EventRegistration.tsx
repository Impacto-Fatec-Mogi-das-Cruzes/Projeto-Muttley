import { useParams } from "react-router-dom";
import { PublicParticipationForm } from "@/components/public/PublicParticipationForm";
import { PublicEventError } from "@/components/public/PublicEventError";
import { useEventRegistration } from "@/hooks/use-event-participation";

export default function EventRegistration() {
  const { eventId } = useParams<{ eventId: string }>();
  const mutation = useEventRegistration(eventId ?? "");

  if (!eventId) {
    return <PublicEventError />;
  }

  return (
    <PublicParticipationForm
      title="Formulário de Inscrição"
      successTitle="Inscrição realizada com sucesso!"
      successDescription="Sua inscrição foi registrada com sucesso. Nos vemos no evento!"
      mutation={mutation}
    />
  );
}
