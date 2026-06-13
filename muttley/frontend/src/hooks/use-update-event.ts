import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateEvent, type UpdateEventFiles } from "@/service/eventService/event-service";
import type { EventUpdateRequest } from "@/models/event/event-update-request";

interface UpdateEventVariables {
  event: EventUpdateRequest;
  files?: UpdateEventFiles;
}

export function useUpdateEvent() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ event, files }: UpdateEventVariables) => updateEvent(event, files),
    onSuccess: (_data, { event }) => {
      queryClient.invalidateQueries({ queryKey: ["event-details", event.id] });
      queryClient.invalidateQueries({ queryKey: ["events"] });
    },
  });
}
