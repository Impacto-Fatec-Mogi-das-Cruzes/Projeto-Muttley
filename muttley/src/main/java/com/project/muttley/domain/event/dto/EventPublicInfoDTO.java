package com.project.muttley.domain.event.dto;

import java.time.LocalDate;
import java.util.UUID;

public record EventPublicInfoDTO(
    UUID id,
    String title,
    LocalDate startDate,
    LocalDate endDate,
    String statusName,
    boolean registrationOpen,
    boolean presenceOpen) {
}
