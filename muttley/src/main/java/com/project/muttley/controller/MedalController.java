package com.project.muttley.controller;

import com.project.muttley.dto.medal.MedalRequestDTO;
import com.project.muttley.dto.medal.MedalResponseDTO;
import com.project.muttley.service.MedalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/medals")
public class MedalController {
    private final MedalService medalService;

    public MedalController(MedalService medalService) {
        this.medalService = medalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedalResponseDTO create(@Valid @RequestBody MedalRequestDTO request) {
        return medalService.create(request);
    }

    @GetMapping
    public List<MedalResponseDTO> findAll() {
        return medalService.findAll();
    }

    @GetMapping("/{id}")
    public MedalResponseDTO findById(@PathVariable Long id) {
        return medalService.findById(id);
    }

    @PutMapping("/{id}")
    public MedalResponseDTO update(@PathVariable Long id, @Valid @RequestBody MedalRequestDTO request) {
        return medalService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        medalService.delete(id);
    }
}
