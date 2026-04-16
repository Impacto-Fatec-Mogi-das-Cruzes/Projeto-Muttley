package com.project.muttley.controller;

import com.project.muttley.dto.user.UserRegistrationRequestDTO;
import com.project.muttley.service.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserRegistrationService userRegistrationService;

    public AuthController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerForm") UserRegistrationRequestDTO request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                    .findFirst()
                    .map(error -> error.getDefaultMessage())
                    .orElse("Dados de cadastro inválidos.");
            redirectAttributes.addFlashAttribute("registerError", message);
            return "redirect:/register";
        }

        try {
            userRegistrationService.register(request);
            redirectAttributes.addFlashAttribute("registerSuccess", "Conta criada com sucesso. Faça seu login.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("registerError", ex.getMessage());
            return "redirect:/register";
        }
    }
}
