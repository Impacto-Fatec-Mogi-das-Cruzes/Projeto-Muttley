import axios from "axios";
import { getToken, clearToken } from "@/lib/auth";

export const api = axios.create({
  baseURL: import.meta.env.VITE_BASE_API,
  headers: { "Content-Type": "application/json" },
});

export const publicApi = axios.create({
  baseURL: import.meta.env.VITE_BASE_API,
  headers: { "Content-Type": "application/json" },
});

api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    const isLoginRequest = error.config?.url?.includes("auth/login");
    if (status === 401 && !isLoginRequest && window.location.pathname !== "/") {
      clearToken();
      window.location.href = "/";
    }
    return Promise.reject(error);
  },
);
