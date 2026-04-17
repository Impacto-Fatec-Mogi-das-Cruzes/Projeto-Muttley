package com.project.muttley.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateUserDTO {
   String name;
   String email;String password;
   List<String> roles;
}
