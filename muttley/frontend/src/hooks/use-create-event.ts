import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  createEvent,
  type CreateEventFiles,
} from "@/service/eventService/event-service";
import type { EventRequest } from "@/models/event/event-request";

interface CreateEventVariables {
  event: EventRequest;
  files: CreateEventFiles;
}

export function useCreateEvent() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ event, files }: CreateEventVariables) => createEvent(event, files),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["events"] });
    },
  });
}
