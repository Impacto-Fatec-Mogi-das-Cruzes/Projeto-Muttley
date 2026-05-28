package com.project.muttley.domain.certificate.dto;

import jakarta.validation.constraints.NotBlank;

public record CertificateGenerateCustomDTO(
    @NotBlank String name,
    @NotBlank String description,
    @NotBlank String responsible,
    @NotBlank String responsibleDescription,
    @NotBlank String backgroundImageUrl,
    @NotBlank String signatureImageUrl,
    @NotBlank String certificateCode,
    @NotBlank String qrCodeUrl) {

}
