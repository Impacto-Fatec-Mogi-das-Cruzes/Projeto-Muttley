package com.project.muttley.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @Email(message = "Formato inválido") @NotBlank(message = "Email obrigatório") String email,

    @NotBlank(message = "Senha obrigatória") String password) {
}
