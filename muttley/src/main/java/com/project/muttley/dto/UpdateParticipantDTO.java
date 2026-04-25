package com.project.muttley.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateParticipantDTO {

    private String name;

    @Email(message = "Email inválido")
    private String email;

    private String cpf;

    private String linkedin;

    private String github;
}