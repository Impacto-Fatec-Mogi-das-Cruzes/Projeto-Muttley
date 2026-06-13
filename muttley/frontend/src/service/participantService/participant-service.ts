import { api } from "@/service/api";
import { unwrapData, unwrapPage } from "@/service/response";
import type { Page } from "@/models/common/page";
import type { Participant } from "@/models/participant/participant";
import type { ParticipantEventHistoryItem } from "@/models/participant/participant-event-history-item";
import type { ParticipantRequest } from "@/models/participant/participant-request";

export interface ListParticipantsParams {
  page: number;
  size: number;
  search?: string;
  sortBy?: "name" | "points";
  direction?: "asc" | "desc";
}

export async function listParticipants({
  page,
  size,
  search,
  sortBy = "name",
  direction = "asc",
}: ListParticipantsParams): Promise<Page<Participant>> {
  const { data } = await api.get("participants", {
    params: {
      page,
      size,
      sort: `${sortBy},${direction}`,
      ...(search ? { search } : {}),
    },
  });
  return unwrapPage<Participant>(data);
}

export async function createParticipant(payload: ParticipantRequest): Promise<Participant> {
  const { data } = await api.post("participants", payload);
  return unwrapData<Participant>(data);
}

export async function getParticipantEventHistory(
  participantId: string,
  params: { page: number; size: number },
): Promise<Page<ParticipantEventHistoryItem>> {
  const { data } = await api.get(`events/participants/${participantId}`, { params });
  return unwrapPage<ParticipantEventHistoryItem>(data);
}
