package com.project.muttley.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.project.muttley.domain.participant.Participant;
import com.project.muttley.domain.participant.dto.ParticipantRequestDTO;
import com.project.muttley.domain.participant.dto.ParticipantResponseDTO;
import com.project.muttley.services.participant.ParticipantService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

  @Autowired
  private ParticipantService participantService;

  @GetMapping
  public ResponseEntity<Page<ParticipantResponseDTO>> get(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "name") String sortBy,
      @RequestParam(defaultValue = "asc") String direction,
      @RequestParam(required = false) String search) {

    return ResponseEntity.ok(
        participantService.get(page, size, sortBy, direction, search));
  }

  @PostMapping
  public ResponseEntity<Participant> postMethodName(@RequestBody @Valid ParticipantRequestDTO data) {

    Participant participant = participantService.create(data);

    return ResponseEntity.ok(participant);
  }

}
