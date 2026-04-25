package com.project.muttley.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateParticipantDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    
    @CPF(message = "CPF inválido")
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;
    
    private String linkedin;

    private String github;
}