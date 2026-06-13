import { keepPreviousData, useQuery } from "@tanstack/react-query";
import {
  getEventDetails,
  type EventDetailsParams,
} from "@/service/eventService/event-service";

export function useEventDetails(eventId: string | undefined, params?: EventDetailsParams) {
  return useQuery({
    queryKey: ["event-details", eventId, params ?? {}],
    queryFn: () => getEventDetails(eventId as string, params),
    enabled: !!eventId,
    placeholderData: keepPreviousData,
  });
}
