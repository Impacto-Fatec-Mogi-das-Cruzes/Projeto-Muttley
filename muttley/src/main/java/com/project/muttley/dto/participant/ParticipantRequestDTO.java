package com.project.muttley.dto.participant;

import com.project.muttley.model.enums.ParticipantType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParticipantRequestDTO(
        @NotBlank(message = "Nome do participante é obrigatório.") String name,
        @NotBlank(message = "E-mail do participante é obrigatório.")
        @Email(message = "Informe um e-mail válido.") String email,
        String ra,
        String linkedinUrl,
        String github,
        @NotNull(message = "Tipo de participante é obrigatório.") ParticipantType participantType
) {
}
