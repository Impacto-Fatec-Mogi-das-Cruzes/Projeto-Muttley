package com.project.muttley.domain.participant.dto;

import java.util.UUID;

public record ParticipantResponseDTO(
    UUID id,
    String name,
    String cpf,
    String email,
    Integer points,
    Integer certificates,
    Integer medals) {

}
