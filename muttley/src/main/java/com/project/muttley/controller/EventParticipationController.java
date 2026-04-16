package com.project.muttley.controller;

import com.project.muttley.dto.participation.EventParticipationRequestDTO;
import com.project.muttley.dto.participation.EventParticipationResponseDTO;
import com.project.muttley.service.EventParticipationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/event-participations")
public class EventParticipationController {
    private final EventParticipationService participationService;

    public EventParticipationController(EventParticipationService participationService) {
        this.participationService = participationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventParticipationResponseDTO create(@Valid @RequestBody EventParticipationRequestDTO request) {
        return participationService.create(request);
    }

    @GetMapping
    public List<EventParticipationResponseDTO> findAll() {
        return participationService.findAll();
    }

    @GetMapping("/{id}")
    public EventParticipationResponseDTO findById(@PathVariable Long id) {
        return participationService.findById(id);
    }

    @PutMapping("/{id}")
    public EventParticipationResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody EventParticipationRequestDTO request) {
        return participationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        participationService.delete(id);
    }
}
