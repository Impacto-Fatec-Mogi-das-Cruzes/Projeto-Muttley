import { useQuery } from "@tanstack/react-query";
import { getEventModalities, getEventTypes } from "@/service/eventService/event-service";

export function useEventTypes() {
  return useQuery({
    queryKey: ["event-types"],
    queryFn: getEventTypes,
    staleTime: Infinity,
  });
}

export function useEventModalities() {
  return useQuery({
    queryKey: ["event-modalities"],
    queryFn: getEventModalities,
    staleTime: Infinity,
  });
}
