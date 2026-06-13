import { publicApi } from "@/service/api";
import type { ParticipationRequest } from "@/models/public/participation-request";

export async function registerForEvent(
  eventId: string,
  payload: ParticipationRequest,
): Promise<void> {
  await publicApi.post(`public/events/${eventId}/registration`, payload);
}

export async function confirmPresence(
  eventId: string,
  payload: ParticipationRequest,
): Promise<void> {
  await publicApi.post(`public/events/${eventId}/presence`, payload);
}
