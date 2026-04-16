package com.project.muttley.dto.participation;

import com.project.muttley.model.enums.ParticipationStatus;
import com.project.muttley.model.enums.RoleInEvent;

import java.time.LocalDateTime;

public record EventParticipationResponseDTO(
        Long id,
        Long eventId,
        String eventName,
        Long participantId,
        String participantName,
        RoleInEvent roleInEvent,
        ParticipationStatus participationStatus,
        LocalDateTime checkInAt,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
