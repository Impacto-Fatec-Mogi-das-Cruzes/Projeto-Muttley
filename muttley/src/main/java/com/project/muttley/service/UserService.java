package com.project.muttley.service;

import com.project.muttley.dto.CreateUserDTO;
import com.project.muttley.dto.UpdateUserDTO;
import com.project.muttley.dto.UserResponseDTO;
import com.project.muttley.model.Role;
import com.project.muttley.model.User;
import com.project.muttley.repository.RoleRepository;
import com.project.muttley.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserResponseDTO create(CreateUserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + dto.getEmail());
        }

        List<Role> roles = resolveRoles(dto.getRoles());

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(roles);
        user.setActive(true);

        return UserResponseDTO.from(userRepository.save(user));
    }

    public List<UserResponseDTO> getAll(String email, String role) {
        List<User> users;

        if (email != null && role != null) {
            users = userRepository.findByEmailContainingIgnoreCaseAndRoles_Name(email, role);
        } else if (email != null) {
            users = userRepository.findByEmailContainingIgnoreCase(email);
        } else if (role != null) {
            users = userRepository.findByRoles_Name(role);
        } else {
            users = userRepository.findAll();
        }

        return users.stream().map(UserResponseDTO::from).toList();
    }

    public Page<UserResponseDTO> getAllPaged(String email, Pageable pageable) {
        Page<User> page = (email != null && !email.isBlank())
            ? userRepository.findByEmailContainingIgnoreCase(email, pageable)
            : userRepository.findAll(pageable);

        return page.map(UserResponseDTO::from);
    }

    public UserResponseDTO getById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: id=" + id));
        return UserResponseDTO.from(user);
    }

    public UserResponseDTO getByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: email=" + email));
        return UserResponseDTO.from(user);
    }

    public UserResponseDTO update(Long id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: id=" + id));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            boolean emailTaken = userRepository.existsByEmail(dto.getEmail())
                && !dto.getEmail().equalsIgnoreCase(user.getEmail());
            if (emailTaken) {
                throw new RuntimeException("Email já cadastrado: " + dto.getEmail());
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            user.setRoles(resolveRoles(dto.getRoles()));
        }

        return UserResponseDTO.from(userRepository.save(user));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado: id=" + id);
        }
        userRepository.deleteById(id);
    }

    private List<Role> resolveRoles(List<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return List.of();
        }
        return roleNames.stream()
            .map(name -> roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role não encontrada: " + name)))
            .toList();
    }
}