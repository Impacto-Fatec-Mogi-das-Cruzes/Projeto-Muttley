package com.project.muttley.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.muttley.domain.event.dto.EventDetailDTO;
import com.project.muttley.domain.event.dto.EventRequestDTO;
import com.project.muttley.domain.event.dto.EventResponseDTO;
import com.project.muttley.domain.event.dto.EventSummaryDTO;
import com.project.muttley.dtos.ApiResponse;
import com.project.muttley.services.event.EventService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

  private final EventService eventService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<EventResponseDTO>> create(
      @Valid @RequestPart("event") EventRequestDTO request,
      @RequestPart("signature") MultipartFile signature,
      HttpServletRequest httpRequest) {
    EventResponseDTO response = eventService.create(request, signature);
    ApiResponse<EventResponseDTO> body = ApiResponse.success(
        HttpStatus.CREATED.value(),
        "Event created successfully",
        httpRequest.getRequestURI(),
        response);
    return ResponseEntity.status(HttpStatus.CREATED).body(body);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<Page<EventSummaryDTO>>> findAll(
      @RequestParam(required = false) String title,
      @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable,
      HttpServletRequest httpRequest) {
    int pageIndex = Math.max(pageable.getPageNumber() - 1, 0);
    Pageable adjusted = PageRequest.of(pageIndex, pageable.getPageSize(), pageable.getSort());
    Page<EventSummaryDTO> response = eventService.findAll(title, adjusted);
    ApiResponse<Page<EventSummaryDTO>> body = ApiResponse.success(
        HttpStatus.OK.value(),
        "Events listed successfully",
        httpRequest.getRequestURI(),
        response);
    return ResponseEntity.ok(body);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<EventResponseDTO>> findById(
      @PathVariable UUID id,
      HttpServletRequest httpRequest) {
    EventResponseDTO response = eventService.findById(id);
    ApiResponse<EventResponseDTO> body = ApiResponse.success(
        HttpStatus.OK.value(),
        "Event found successfully",
        httpRequest.getRequestURI(),
        response);
    return ResponseEntity.ok(body);
  }

  @GetMapping("/{id}/details")
  public ResponseEntity<ApiResponse<EventDetailDTO>> findDetails(
      @PathVariable UUID id,
      @RequestParam(required = false) String role,
      @RequestParam(defaultValue = "name") String sortBy,
      @RequestParam(defaultValue = "asc") String direction,
      @PageableDefault(size = 10) Pageable pageable,
      HttpServletRequest httpRequest) {
    int pageIndex = Math.max(pageable.getPageNumber(), 0);
    Sort sort = buildParticipantSort(sortBy, direction);
    Pageable adjusted = PageRequest.of(pageIndex, pageable.getPageSize(), sort);
    EventDetailDTO response = eventService.findDetails(id, role, adjusted);
    return ResponseEntity.ok(ApiResponse.success(
        HttpStatus.OK.value(),
        "Detalhes do evento carregados com sucesso",
        httpRequest.getRequestURI(),
        response));
  }

  private Sort buildParticipantSort(String sortBy, String direction) {
    Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
        ? Sort.Direction.DESC
        : Sort.Direction.ASC;
    String field = switch (sortBy == null ? "" : sortBy.toLowerCase()) {
      case "registrationdate", "registeredat", "data", "datainscricao" -> "registeredAt";
      case "role", "cargo" -> "participantType.name";
      case "status" -> "present";
      default -> "participant.name";
    };
    return Sort.by(sortDirection, field);
  }

  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<EventResponseDTO>> update(
      @PathVariable UUID id,
      @Valid @RequestPart("event") EventRequestDTO request,
      @RequestPart(value = "signature", required = false) MultipartFile signature,
      HttpServletRequest httpRequest) {
    EventResponseDTO response = eventService.update(id, request, signature);
    ApiResponse<EventResponseDTO> body = ApiResponse.success(
        HttpStatus.OK.value(),
        "Event updated successfully",
        httpRequest.getRequestURI(),
        response);
    return ResponseEntity.ok(body);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> delete(
      @PathVariable UUID id,
      HttpServletRequest httpRequest) {
    eventService.delete(id);
    ApiResponse<Object> body = ApiResponse.success(
        HttpStatus.OK.value(),
        "Event deleted successfully",
        httpRequest.getRequestURI(),
        null);
    return ResponseEntity.ok(body);
  }

  @PatchMapping("/{id}/finalize")
  public ResponseEntity<ApiResponse<EventResponseDTO>> finalizeEvent(
      @PathVariable UUID id,
      HttpServletRequest httpRequest) {
    EventResponseDTO response = eventService.finalizeEvent(id);
    ApiResponse<EventResponseDTO> body = ApiResponse.success(
        HttpStatus.OK.value(),
        "Event finalized successfully",
        httpRequest.getRequestURI(),
        response);
    return ResponseEntity.ok(body);
  }
}
