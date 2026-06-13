import { useMutation, useQueryClient } from "@tanstack/react-query";
import { deleteEvent } from "@/service/eventService/event-service";

export function useDeleteEvent() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (eventId: string) => deleteEvent(eventId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["events"] });
    },
  });
}
