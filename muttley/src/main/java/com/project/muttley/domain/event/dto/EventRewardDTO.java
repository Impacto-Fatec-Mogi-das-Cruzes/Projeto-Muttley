package com.project.muttley.domain.event.dto;

import java.util.List;
import java.util.UUID;

import com.project.muttley.validation.MaxKeywords;

public record EventRewardDTO(
    List<UUID> participantIds,
    String description,
    @MaxKeywords(max = 10) String competencies) {

}
