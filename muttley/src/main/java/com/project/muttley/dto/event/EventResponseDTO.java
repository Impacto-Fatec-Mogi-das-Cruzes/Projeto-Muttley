package com.project.muttley.dto.event;

import com.project.muttley.model.enums.EventStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record EventResponseDTO(
        Long id,
        String name,
        String description,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String location,
        String resourcesNeeded,
        EventStatus status,
        Long createdByUserId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
