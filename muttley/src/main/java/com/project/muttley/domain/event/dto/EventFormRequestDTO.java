package com.project.muttley.domain.event.dto;

import com.project.muttley.validation.ValidCpf;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EventFormRequestDTO(
    @NotBlank(message = "Nome é obrigatório") String name,
    @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,
    @NotBlank(message = "CPF é obrigatório") @ValidCpf String cpf) {
}
