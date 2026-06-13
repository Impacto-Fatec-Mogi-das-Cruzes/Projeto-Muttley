import type { EventRequest } from "@/models/event/event-request";

export interface EventUpdateRequest extends EventRequest {
  id: string;
}
