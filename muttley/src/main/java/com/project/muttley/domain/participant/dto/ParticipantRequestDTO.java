package com.project.muttley.domain.participant.dto;

import com.project.muttley.validation.ValidCpf;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ParticipantRequestDTO(

    @NotBlank(message = "Name is required") String name,

    @NotBlank(message = "Email is required") @Email(message = "Invalid email") String email,

    @NotBlank(message = "CPF is required") @ValidCpf String cpf) {
}