package com.project.muttley.domain.event.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.project.muttley.validation.MaxKeywords;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EventRequestDTO(
    @NotBlank(message = "Título é obrigatório") String title,
    @NotNull(message = "Data inicial é obrigatória") LocalDate startDate,
    LocalDate endDate,
    Integer workload,
    Long points,
    @NotBlank(message = "Tipo do evento é obrigatório") String typeId,
    String subject,
    @MaxKeywords(max = 10) String keywords,
    String description,
    @NotBlank(message = "Modalidade é obrigatória") String modalityId,
    @NotBlank(message = "Endereço ou link é obrigatório") String addressOrLink,
    Integer capacity,
    @NotEmpty(message = "Informe ao menos um organizador") List<UUID> organizerIds,
    List<UUID> speakerIds,
    List<UUID> sponsorIds,
    String nameSignature,
    String positionSignature) {
}
