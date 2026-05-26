package com.project.muttley.domain.event.dto;

import java.util.UUID;

public record EventTypeItemDTO(
    UUID id,
    String name) {
}
