import { api } from "@/service/api";
import { unwrapArray, unwrapData, unwrapPage } from "@/service/response";
import type { Page } from "@/models/common/page";
import type { EventOption } from "@/models/event/event-option";
import type { EventListItem } from "@/models/event/event-list-item";
import type { EventRequest } from "@/models/event/event-request";
import type { EventDetails } from "@/models/event/event-details";
import type { EventUpdateRequest } from "@/models/event/event-update-request";
import type { RewardRequest } from "@/models/event/reward-request";

export interface ListEventsParams {
  page: number;
  size: number;
  sortBy: "title" | "points";
  direction: "asc" | "desc";
}

export async function listEvents({
  page,
  size,
  sortBy,
  direction,
}: ListEventsParams): Promise<Page<EventListItem>> {
  const { data } = await api.get("events", {
    params: { page, size, sort: `${sortBy},${direction}` },
  });
  return unwrapPage<EventListItem>(data);
}

export async function getEventTypes(): Promise<EventOption[]> {
  const { data } = await api.get("event-types");
  return unwrapArray<EventOption>(data);
}

export async function getEventModalities(): Promise<EventOption[]> {
  const { data } = await api.get("event-modalities");
  return unwrapArray<EventOption>(data);
}

export interface CreateEventFiles {
  background: File;
  signature: File;
}

export async function createEvent(event: EventRequest, files: CreateEventFiles): Promise<void> {
  const formData = new FormData();
  formData.append("background", files.background);
  formData.append("signature", files.signature);
  formData.append("event", new Blob([JSON.stringify(event)], { type: "application/json" }));

  await api.post("events", formData, {
    headers: { "Content-Type": undefined },
  });
}

export interface EventDetailsParams {
  page?: number;
  size?: number;
  sortBy?: string;
  direction?: "asc" | "desc";
}

export async function getEventDetails(
  eventId: string,
  params?: EventDetailsParams,
): Promise<EventDetails> {
  const { sortBy, direction, ...rest } = params ?? {};
  const { data } = await api.get(`events/${eventId}/details`, {
    params: {
      ...rest,
      ...(sortBy ? { sort: `${sortBy},${direction ?? "asc"}` } : {}),
    },
  });
  return unwrapData<EventDetails>(data);
}

export interface UpdateEventFiles {
  signature?: File;
  background?: File;
}

export async function updateEvent(
  event: EventUpdateRequest,
  files: UpdateEventFiles = {},
): Promise<void> {
  const formData = new FormData();
  if (files.signature) {
    formData.append("signature", files.signature);
  }
  if (files.background) {
    formData.append("background", files.background);
  }
  formData.append("event", new Blob([JSON.stringify(event)], { type: "application/json" }));

  await api.put(`events/${event.id}`, formData, {
    headers: { "Content-Type": undefined },
  });
}

export async function deleteEvent(eventId: string): Promise<void> {
  await api.delete(`events/${eventId}`);
}

export async function finalizeEvent(eventId: string): Promise<void> {
  await api.patch(`events/${eventId}/finalize`);
}

export async function rewardParticipants(eventId: string, payload: RewardRequest): Promise<void> {
  await api.post(`events/${eventId}/reward`, payload);
}
