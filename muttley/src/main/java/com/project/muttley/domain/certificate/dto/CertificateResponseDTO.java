package com.project.muttley.domain.certificate.dto;

import java.util.UUID;

public record CertificateResponseDTO(
    UUID id,
    String participantName,
    String participantEmail,
    String certificateUrl) {

}
