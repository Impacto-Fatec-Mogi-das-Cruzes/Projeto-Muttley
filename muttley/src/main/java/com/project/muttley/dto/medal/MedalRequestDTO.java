package com.project.muttley.dto.medal;

import com.project.muttley.model.enums.RoleInEvent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedalRequestDTO(
        @NotNull(message = "Evento é obrigatório.") Long eventId,
        @NotBlank(message = "Nome da medalha é obrigatório.") String name,
        String description,
        @NotBlank(message = "Categoria é obrigatória.") String category,
        @NotNull(message = "Pontuação é obrigatória.") Integer score,
        @NotNull(message = "Função alvo é obrigatória.") RoleInEvent targetRole,
        @NotNull(message = "Status ativo/inativo é obrigatório.") Boolean active
) {
}
