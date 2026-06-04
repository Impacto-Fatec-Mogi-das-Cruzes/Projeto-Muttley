package com.project.muttley.domain.event.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

public record EventDetailDTO(
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
    String subject,
    String keywords,
    String description,
    UUID modalityId,
    String modalityName,
    String addressOrLink,
    Integer capacity,
    List<EventStaffDTO> organizers,
    List<EventStaffDTO> speakers,
    List<EventStaffDTO> sponsors,
    String imageBackgroundUrl,
    String signatureImageUrl,
    String nameSignature,
    String positionSignature,
    UUID statusId,
    String statusName,
    String registrationUrl,
    String presenceUrl,
    String registrationQrCodeUrl,
    String presenceQrCodeUrl,
    long registeredCount,
    long presentCount,
    Page<EventParticipantItemDTO> participants,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
