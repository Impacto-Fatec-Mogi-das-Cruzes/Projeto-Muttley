package com.project.muttley.controller;

import com.project.muttley.dto.participant.ParticipantRequestDTO;
import com.project.muttley.dto.participant.ParticipantResponseDTO;
import com.project.muttley.service.ParticipantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/participants")
public class ParticipantController {
    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipantResponseDTO create(@Valid @RequestBody ParticipantRequestDTO request) {
        return participantService.create(request);
    }

    @GetMapping
    public List<ParticipantResponseDTO> findAll() {
        return participantService.findAll();
    }

    @GetMapping("/{id}")
    public ParticipantResponseDTO findById(@PathVariable Long id) {
        return participantService.findById(id);
    }

    @PutMapping("/{id}")
    public ParticipantResponseDTO update(@PathVariable Long id, @Valid @RequestBody ParticipantRequestDTO request) {
        return participantService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        participantService.delete(id);
    }
}
