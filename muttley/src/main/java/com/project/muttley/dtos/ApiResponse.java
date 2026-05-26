package com.project.muttley.dtos;

import java.time.LocalDateTime;

public record ApiResponse<T>(
    int status,
    String message,
    String path,
    T data,
    LocalDateTime timestamp) {

  public static <T> ApiResponse<T> success(int status, String message, String path, T data) {
    return new ApiResponse<>(status, message, path, data, LocalDateTime.now());
  }
}
