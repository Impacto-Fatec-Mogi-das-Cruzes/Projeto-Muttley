package com.project.muttley.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.muttley.dto.CreateUserDTO;
import com.project.muttley.dto.UserResponseDTO;
import com.project.muttley.model.User;
import com.project.muttley.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody CreateUserDTO data) {
        return userService.createUser(
                data.getName(),
                data.getEmail(),
                data.getPassword(),
                data.getRoles()
        );
    }

    @GetMapping
    public List<UserResponseDTO> getUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role
    ) {
        return userService.getUsers(email, role).stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRoles().stream()
                                .map(r -> r.getName())
                                .toList()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getById(@PathVariable Long id) {
        User user = userService.getById(id);

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles().stream().map(r -> r.getName()).toList()
        );
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(
            @PathVariable Long id,
            @RequestBody CreateUserDTO data
    ) {
        User user = userService.updateUser(id, data);

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles().stream().map(r -> r.getName()).toList()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}