import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { listEvents, type ListEventsParams } from "@/service/eventService/event-service";

export function useEvents(params: ListEventsParams) {
  return useQuery({
    queryKey: ["events", params],
    queryFn: () => listEvents(params),
    placeholderData: keepPreviousData,
  });
}
