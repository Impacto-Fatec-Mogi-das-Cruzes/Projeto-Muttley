package com.project.muttley.domain.event.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

public record EventDetailDTO(
    UUID id,
    String title,
    LocalDate startDate,
    LocalDate endDate,
    Integer workload,
    Long points,
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
