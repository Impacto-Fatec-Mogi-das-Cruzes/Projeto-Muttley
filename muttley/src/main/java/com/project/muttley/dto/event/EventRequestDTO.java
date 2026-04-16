package com.project.muttley.dto.event;

import com.project.muttley.model.enums.EventStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventRequestDTO(
        @NotBlank(message = "Nome do evento é obrigatório.") String name,
        String description,
        @NotNull(message = "Data do evento é obrigatória.") LocalDate date,
        @NotNull(message = "Hora de início é obrigatória.") LocalTime startTime,
        LocalTime endTime,
        @NotBlank(message = "Local do evento é obrigatório.") String location,
        String resourcesNeeded,
        @NotNull(message = "Status do evento é obrigatório.") EventStatus status,
        @NotNull(message = "Usuário criador é obrigatório.") Long createdByUserId
) {
}
