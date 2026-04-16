package com.project.muttley.controller;

import com.project.muttley.dto.event.EventRequestDTO;
import com.project.muttley.dto.event.EventResponseDTO;
import com.project.muttley.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDTO create(@Valid @RequestBody EventRequestDTO request) {
        return eventService.create(request);
    }

    @GetMapping
    public List<EventResponseDTO> findAll() {
        return eventService.findAll();
    }

    @GetMapping("/{id}")
    public EventResponseDTO findById(@PathVariable Long id) {
        return eventService.findById(id);
    }

    @PutMapping("/{id}")
    public EventResponseDTO update(@PathVariable Long id, @Valid @RequestBody EventRequestDTO request) {
        return eventService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        eventService.delete(id);
    }
}
