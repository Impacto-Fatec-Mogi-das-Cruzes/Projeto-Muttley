package com.project.muttley.dto;

import com.project.muttley.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private List<String> roles;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponseDTO from(User user) {
        List<String> roleNames = user.getRoles() == null
            ? List.of()
            : user.getRoles().stream().map(r -> r.getName()).toList();

        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            roleNames,
            user.getActive(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}