package com.project.muttley.controllers;

import java.net.http.HttpRequest;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.muttley.domain.certificate.Certificate;
import com.project.muttley.domain.certificate.dto.CertificateResponseDTO;
import com.project.muttley.dtos.ApiResponse;
import com.project.muttley.repositories.CertificateRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/public/certificate")
public class CertificateController {

  @Autowired
  private CertificateRepository certificateRepository;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<CertificateResponseDTO>> getCertificateInfo(
      @PathVariable UUID id,
      HttpServletRequest http) {

    CertificateResponseDTO responseDTO = certificateRepository.findById(id)
        .map(c -> new CertificateResponseDTO(
            c.getId(),
            c.getParticipant().getName(),
            c.getParticipant().getEmail(),
            c.getFileUrl()))
        .orElseThrow(() -> new RuntimeException("Certificado não encontrado"));

    ApiResponse<CertificateResponseDTO> body = ApiResponse.success(
        HttpStatus.OK.value(),
        "Dados do Certificado exibidos com sucesso!",
        http.getRequestURI(),
        responseDTO);

    return ResponseEntity.ok(body);
  }

}
