package com.project.muttley.domain.certificate.dto;

import java.util.UUID;

public record CertificateRequestDTO(
    UUID participantId,
    UUID eventId) {

}
