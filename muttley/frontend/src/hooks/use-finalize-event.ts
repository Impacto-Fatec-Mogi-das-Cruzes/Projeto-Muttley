import { useMutation, useQueryClient } from "@tanstack/react-query";
import { finalizeEvent } from "@/service/eventService/event-service";

export function useFinalizeEvent() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (eventId: string) => finalizeEvent(eventId),
    onSuccess: (_data, eventId) => {
      queryClient.invalidateQueries({ queryKey: ["event-details", eventId] });
      queryClient.invalidateQueries({ queryKey: ["events"] });
    },
  });
}
