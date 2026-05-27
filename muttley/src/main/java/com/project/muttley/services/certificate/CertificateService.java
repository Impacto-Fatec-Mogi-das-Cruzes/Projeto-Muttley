package com.project.muttley.services.certificate;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.muttley.domain.certificate.Certificate;
import com.project.muttley.domain.certificate.dto.CertificateRequestDTO;
import com.project.muttley.domain.event.Event;
import com.project.muttley.domain.participant.Participant;
import com.project.muttley.repositories.CertificateRepository;
import com.project.muttley.repositories.EventRepository;
import com.project.muttley.repositories.ParticipantRepository;

@Service
public class CertificateService {

  @Autowired
  private CertificateRepository certificateRepository;

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private ParticipantRepository participantRepository;

  public Certificate create(
      CertificateRequestDTO dto) {

    Certificate certificate = new Certificate();

    Event event = eventRepository.findById(dto.eventId())
        .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

    Participant participant = participantRepository.findById(dto.participantId())
        .orElseThrow(() -> new RuntimeException("Participante não encontrado"));

    certificate.setEvent(event);
    certificate.setParticipant(participant);

    return certificateRepository.save(certificate);

  }

  public Certificate updateUrlCertificate(UUID id, String urlCertificate) {

    Certificate certificate = certificateRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Certificado não encontrado"));

    certificate.setFileUrl(urlCertificate);

    return certificateRepository.save(certificate);

  }

}
