import type { ApiResponse } from "@/models/common/api-response";
import type { Page } from "@/models/common/page";

type MaybeWrapped<T> = T | ApiResponse<T>;

const isObject = (value: unknown): value is Record<string, unknown> =>
  typeof value === "object" && value !== null;

export function unwrapArray<T>(payload: MaybeWrapped<T[]>): T[] {
  if (Array.isArray(payload)) return payload;
  if (isObject(payload) && Array.isArray(payload.data)) return payload.data as T[];
  return [];
}

export function unwrapPage<T>(payload: MaybeWrapped<Page<T>>): Page<T> {
  if (isObject(payload) && Array.isArray(payload.content)) {
    return payload as Page<T>;
  }
  if (isObject(payload) && isObject(payload.data)) {
    return payload.data as unknown as Page<T>;
  }
  return payload as Page<T>;
}

export function unwrapData<T>(payload: MaybeWrapped<T>): T {
  if (isObject(payload) && "data" in payload && !("content" in payload)) {
    return payload.data as T;
  }
  return payload as T;
}
