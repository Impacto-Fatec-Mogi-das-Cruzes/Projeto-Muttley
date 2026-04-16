package com.project.muttley.dto.medal;

import com.project.muttley.model.enums.RoleInEvent;

import java.time.LocalDateTime;

public record MedalResponseDTO(
        Long id,
        Long eventId,
        String eventName,
        String name,
        String description,
        String category,
        Integer score,
        RoleInEvent targetRole,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
