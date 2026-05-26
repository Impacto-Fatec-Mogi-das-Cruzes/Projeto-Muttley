package com.project.muttley.domain.event.dto;

import java.util.UUID;

public record EventStaffDTO(
    UUID participantId,
    String name,
    String cpf,
    String email,
    String roleName) {
}
