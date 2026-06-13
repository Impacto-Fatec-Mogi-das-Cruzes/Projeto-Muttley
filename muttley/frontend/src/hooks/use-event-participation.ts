import { useMutation } from "@tanstack/react-query";
import {
  confirmPresence,
  registerForEvent,
} from "@/service/publicEventService/public-event-service";
import type { ParticipationRequest } from "@/models/public/participation-request";

export function useEventRegistration(eventId: string) {
  return useMutation({
    mutationFn: (payload: ParticipationRequest) => registerForEvent(eventId, payload),
  });
}

export function useEventPresence(eventId: string) {
  return useMutation({
    mutationFn: (payload: ParticipationRequest) => confirmPresence(eventId, payload),
  });
}
