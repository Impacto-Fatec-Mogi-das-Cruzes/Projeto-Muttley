package com.project.muttley.domain.event.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record EventResponseDTO(
    UUID id,
    String title,
    LocalDate startDate,
    LocalTime startHour,
    LocalDate endDate,
    LocalTime endHour,
    Integer workload,
    Integer points,
    UUID typeId,
    String typeName,
    UUID modalityId,
    String modalityName,
    String subject,
    String keywords,
    String description,
    Integer capacity,
    String addressOrLink,
    String backgroundImageUrl,
    String signatureImageUrl,
    String nameSignature,
    String positionSignature,
    UUID statusId,
    String statusName,
    String registrationUrl,
    String presenceUrl,
    String registrationQrCodeUrl,
    String presenceQrCodeUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
