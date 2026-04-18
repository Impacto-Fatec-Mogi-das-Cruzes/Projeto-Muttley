package com.project.muttley.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {

    private String name;

    @Email(message = "Email inválido")
    private String email;

    private String password;

    private List<String> roles;
}