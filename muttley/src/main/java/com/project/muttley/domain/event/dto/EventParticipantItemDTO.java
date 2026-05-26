package com.project.muttley.domain.event.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventParticipantItemDTO(
    UUID id,
    UUID participantId,
    String name,
    String cpf,
    String email,
    String roleName,
    LocalDateTime registeredAt,
    LocalDateTime checkInAt,
    boolean present,
    String registrationStatus) {
}
