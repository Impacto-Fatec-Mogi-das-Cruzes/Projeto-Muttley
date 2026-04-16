package com.project.muttley.dto.participant;

import com.project.muttley.model.enums.ParticipantType;

import java.time.LocalDateTime;

public record ParticipantResponseDTO(
        Long id,
        String name,
        String email,
        String ra,
        String linkedinUrl,
        String github,
        ParticipantType participantType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
