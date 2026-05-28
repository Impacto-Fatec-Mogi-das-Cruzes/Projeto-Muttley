package com.project.muttley.domain.certificate.dto;

import jakarta.validation.constraints.NotBlank;

public record CertificateGenerateDTO(
    @NotBlank String name,
    @NotBlank String presentation,
    @NotBlank String event,
    @NotBlank String day,
    @NotBlank Integer hours,
    @NotBlank String responsible,
    @NotBlank String responsibleDescription,
    @NotBlank String backgroundImageUrl,
    @NotBlank String signatureImageUrl,
    @NotBlank String certificateCode,
    @NotBlank String qrCodeUrl) {

}
