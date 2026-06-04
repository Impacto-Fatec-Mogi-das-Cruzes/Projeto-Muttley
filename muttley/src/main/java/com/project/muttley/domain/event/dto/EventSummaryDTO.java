package com.project.muttley.domain.event.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record EventSummaryDTO(
    UUID id,
    String title,
    LocalDate startDate,
    LocalTime startHour,
    LocalDate endDate,
    LocalTime endHour,
    String statusName,
    String typeName,
    String modalityName,
    Integer points) {
}
