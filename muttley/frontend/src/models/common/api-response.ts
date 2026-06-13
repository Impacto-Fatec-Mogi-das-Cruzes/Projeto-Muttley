export interface ApiResponse<T> {
  status: number;
  message: string;
  path: string;
  data: T;
  timestamp: string;
}
