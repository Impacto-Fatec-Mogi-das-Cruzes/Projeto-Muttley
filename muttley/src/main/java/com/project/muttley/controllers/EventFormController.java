package com.project.muttley.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.muttley.domain.event.dto.EventFormRequestDTO;
import com.project.muttley.domain.event.dto.EventFormResponseDTO;
import com.project.muttley.domain.event.dto.EventPublicInfoDTO;
import com.project.muttley.dtos.ApiResponse;
import com.project.muttley.services.event.EventFormService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/events")
@RequiredArgsConstructor
public class EventFormController {

  private final EventFormService eventFormService;

  @GetMapping("/{eventId}")
  public ResponseEntity<ApiResponse<EventPublicInfoDTO>> getPublicInfo(
      @PathVariable UUID eventId,
      HttpServletRequest httpRequest) {
    EventPublicInfoDTO response = eventFormService.getPublicInfo(eventId);
    return ResponseEntity.ok(ApiResponse.success(
        HttpStatus.OK.value(),
        "Event information retrieved successfully",
        httpRequest.getRequestURI(),
        response));
  }

  @PostMapping("/{eventId}/registration")
  public ResponseEntity<ApiResponse<EventFormResponseDTO>> register(
      @PathVariable UUID eventId,
      @Valid @RequestBody EventFormRequestDTO request,
      HttpServletRequest httpRequest) {
    EventFormResponseDTO response = eventFormService.register(eventId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
        HttpStatus.CREATED.value(),
        response.message(),
        httpRequest.getRequestURI(),
        response));
  }

  @PostMapping("/{eventId}/presence")
  public ResponseEntity<ApiResponse<EventFormResponseDTO>> confirmPresence(
      @PathVariable UUID eventId,
      @Valid @RequestBody EventFormRequestDTO request,
      HttpServletRequest httpRequest) {
    EventFormResponseDTO response = eventFormService.confirmPresence(eventId, request);
    return ResponseEntity.ok(ApiResponse.success(
        HttpStatus.OK.value(),
        response.message(),
        httpRequest.getRequestURI(),
        response));
  }
}
