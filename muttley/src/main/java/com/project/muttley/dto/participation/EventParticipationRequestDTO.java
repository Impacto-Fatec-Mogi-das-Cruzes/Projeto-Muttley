package com.project.muttley.dto.participation;

import com.project.muttley.model.enums.ParticipationStatus;
import com.project.muttley.model.enums.RoleInEvent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventParticipationRequestDTO(
        @NotNull(message = "Evento é obrigatório.") Long eventId,
        @NotNull(message = "Participante é obrigatório.") Long participantId,
        @NotNull(message = "Função no evento é obrigatória.") RoleInEvent roleInEvent,
        @NotNull(message = "Status de participação é obrigatório.") ParticipationStatus participationStatus,
        LocalDateTime checkInAt,
        String notes
) {
}
