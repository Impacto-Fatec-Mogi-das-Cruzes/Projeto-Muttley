package com.project.muttley.domain.event.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventFormResponseDTO(
    UUID eventId,
    UUID participantId,
    String participantName,
    String cpf,
    String message,
    LocalDateTime registeredAt,
    LocalDateTime checkInAt,
    boolean present) {
}
