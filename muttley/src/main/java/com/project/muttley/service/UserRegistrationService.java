package com.project.muttley.service;

import com.project.muttley.dto.user.UserRegistrationRequestDTO;
import com.project.muttley.model.User;
import com.project.muttley.model.enums.UserRole;
import com.project.muttley.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(UserRegistrationRequestDTO request) {
        String email = request.email().trim().toLowerCase();
        String username = request.username().trim();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Já existe uma conta com este e-mail.");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Nome de usuário já está em uso.");
        }

        User user = new User();
        user.setUsername(username);
        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.STAFF);

        userRepository.save(user);
    }
}
