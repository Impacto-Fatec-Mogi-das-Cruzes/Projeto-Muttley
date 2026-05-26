package com.project.muttley.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.muttley.domain.event.dto.EventTypeItemDTO;
import com.project.muttley.dtos.ApiResponse;
import com.project.muttley.services.event.EventTypeService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/event-types")
@RequiredArgsConstructor
public class EventTypeController {

  private final EventTypeService eventTypeService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<EventTypeItemDTO>>> findAll(HttpServletRequest httpRequest) {
    List<EventTypeItemDTO> response = eventTypeService.findAll();
    return ResponseEntity.ok(ApiResponse.success(
        HttpStatus.OK.value(),
        "Event types listed successfully",
        httpRequest.getRequestURI(),
        response));
  }
}
