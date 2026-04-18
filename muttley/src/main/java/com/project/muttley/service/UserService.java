package com.project.muttley.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.muttley.dto.CreateUserDTO;
import com.project.muttley.dto.UpdateUserDTO;
import com.project.muttley.model.Role;
import com.project.muttley.model.User;
import com.project.muttley.repository.RoleRepository;
import com.project.muttley.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(String name, String email, String rawPassword, List<String> roleNames) {

        List<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role não encontrada: " + roleName)))
                .toList();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(roles);
        user.setActive(true);

        return userRepository.save(user);
    }

    public List<User> getUsers(String email, String role) {

        if (email != null && role != null) {
            return userRepository.findByEmailContainingIgnoreCaseAndRoles_Name(email, role);
        }

        if (email != null) {
            return userRepository.findByEmailContainingIgnoreCase(email);
        }

        if (role != null) {
            return userRepository.findByRoles_Name(role);
        }

        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public User updateUser(Long id, UpdateUserDTO data) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Nome
        if (data.getName() != null && !data.getName().isBlank()) {
            user.setName(data.getName());
        }

        // Email
        if (data.getEmail() != null && !data.getEmail().isBlank()) {
            user.setEmail(data.getEmail());
        }

        // Senha
        if (data.getPassword() != null && !data.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(data.getPassword()));
        }

        // Roles
        if (data.getRoles() != null && !data.getRoles().isEmpty()) {
            List<Role> roles = data.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role não encontrada: " + roleName)))
                    .toList();

            user.setRoles(roles);
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }

        userRepository.deleteById(id);
    }

}
