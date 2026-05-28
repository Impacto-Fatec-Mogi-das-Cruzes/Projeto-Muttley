package com.project.muttley.domain.event.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.project.muttley.domain.event.eventmodality.EventModality;
import com.project.muttley.domain.event.eventstatus.EventStatus;

public record EventParticipantDetailDTO(
    UUID id,
    String title,
    EventModality eventModality,
    LocalDate dateStart,
    String dateEnd,
    EventStatus status) {

}
