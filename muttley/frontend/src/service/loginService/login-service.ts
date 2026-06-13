import { api } from "@/service/api";
import type { LoginRequest } from "@/models/auth/login-request";
import type { LoginResponse } from "@/models/auth/login-response";

export async function login(payload: LoginRequest): Promise<LoginResponse> {
  const { data } = await api.post<string>("auth/login", payload);
  return { token: data };
}
