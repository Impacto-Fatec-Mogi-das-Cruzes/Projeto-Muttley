package com.project.muttley.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequestDTO(
        @NotBlank(message = "O nome de usuário é obrigatório.")
        @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres.")
        String username,
        @NotBlank(message = "O nome completo é obrigatório.")
        String name,
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.")
        String email,
        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String password
) {
}
