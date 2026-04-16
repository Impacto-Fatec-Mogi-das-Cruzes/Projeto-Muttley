package com.project.muttley.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.muttley.dto.LoginRequestDTO;
import com.project.muttley.dto.LoginResponseDTO;
import com.project.muttley.model.User;
import com.project.muttley.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping
    public LoginResponseDTO login(@RequestBody LoginRequestDTO data) {
        Optional<User> userOpt = userService.getByEmail(data.getEmail());

        User user = userOpt.orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos"));

        if (!new BCryptPasswordEncoder().matches(data.getPassword(), user.getPassword())) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        return new LoginResponseDTO(
                user.getEmail(),
                user.getRoles().stream()
                        .map(role -> role.getName())
                        .toList());
    }

}
