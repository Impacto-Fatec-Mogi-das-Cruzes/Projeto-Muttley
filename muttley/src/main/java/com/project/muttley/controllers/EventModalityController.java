package com.project.muttley.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.muttley.domain.event.dto.EventModalityItemDTO;
import com.project.muttley.dtos.ApiResponse;
import com.project.muttley.services.event.EventModalityService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/event-modalities")
@RequiredArgsConstructor
public class EventModalityController {

  private final EventModalityService eventModalityService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<EventModalityItemDTO>>> findAll(HttpServletRequest httpRequest) {
    List<EventModalityItemDTO> response = eventModalityService.findAll();
    return ResponseEntity.ok(ApiResponse.success(
        HttpStatus.OK.value(),
        "Event modalities listed successfully",
        httpRequest.getRequestURI(),
        response));
  }
}
