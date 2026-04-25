package com.project.muttley.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.muttley.dto.ParticipantResponseDTO;
import com.project.muttley.service.ParticipantService;

@RestController
@RequestMapping("/api/participants")
public class ParticipationController {

    @Autowired ParticipantService participantService;

    @GetMapping
    public ResponseEntity<Page<ParticipantResponseDTO>> get(
        @RequestParam(required = false) String cpf,
        @RequestParam(required = false) String name,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id,asc") String[] sort
    ) {

        Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        return ResponseEntity.ok(participantService.get(cpf, name, pageable));
    }

    //TODO: Create participant related with an event or just link participant with an event

}